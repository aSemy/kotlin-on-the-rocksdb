package buildsrc.kotr

import buildsrc.ext.lowercaseFirstChar
import buildsrc.ext.uppercaseFirstChar
import java.io.File
import kotlinx.metadata.*
import kotlinx.metadata.klib.KlibModuleMetadata
import org.jetbrains.kotlin.library.ToolingSingleFileKlibResolveStrategy
import org.jetbrains.kotlin.library.resolveSingleFileKlib
import org.jetbrains.kotlin.util.DummyLogger
import org.jetbrains.kotlin.konan.file.File as KonanFile


object KLibProcessor {

  fun generateRdbInterop(library: File): List<Pair<String, String>> {
    // parse the generated klib
    val metadata = readLibraryMetadata(library)

    // extract all functions from the generated klib
    val functions = metadata.fragments.flatMap { it.pkg?.functions ?: emptyList() }

    // get all 'creatables' - RDB objects that have a `rocksdb_..._create()` function
    val creatables = functions
      .filter { it.name.endsWith("_create") && it.returnType.classifier.kotlinType() != "Unit" }
      .map { fn ->
        RdbCreateable(
          fn.name.substringAfter("rocksdb_").substringBefore("_create"),
          fn,
        )
      }

    return creatables
      .associateWith { creatable ->
        // get the functions associated with each creatable
        functions.filter { fn ->
          fn.name.startsWith("rocksdb_${creatable.name}_") &&
              (fn.valueParameters.any { it.sameTypeAs(creatable.type) }
                  ||
                  fn.returnType.kotlinType() == creatable.type.kotlinType()
                  )
        }
      }.map { (creatable, fns) ->
        // each creatable has properties that are equivalent to `val`, `var`, or a get/set function


        /**
         * If the [KMFunction] is a setter, return it along with its name (the function name
         * without the prefix). Else, returns `null`
         */
        fun KmFunction.toSetter(): Pair<String, KmFunction>? {
          // each setter will start with this prefix:
          val setterFnBaseName = "rocksdb_${creatable.name}_set_"

          val isSetter = name.startsWith(setterFnBaseName)
              // a setter must have two params, the first being the creatable (the second can be anything)
              && valueParameters.size == 2
              && valueParameters.first().sameTypeAs(creatable.type)
              && returnType.isUnit()

          return when {
            isSetter -> name.substringAfter(setterFnBaseName) to this
            else     -> null
          }
        }

        /**
         * If the [KMFunction] is a getter, return it along with its name (the function name
         * without the prefix). Else, returns `null`
         */
        fun KmFunction.toGetter(): Pair<String, KmFunction>? {
          // each getter will start with this prefix:
          val getterFnBaseName = "rocksdb_${creatable.name}_get_"

          // a getter can only have one parameter, which is the creatable being queried
          val isGetter = name.startsWith(getterFnBaseName)
              && valueParameters.size == 1
              && valueParameters.first().sameTypeAs(creatable.type)

          return when {
            isGetter -> name.substringAfter(getterFnBaseName) to this
            else     -> null
          }
        }

        val setters = fns.mapNotNull { it.toSetter() }
        val getters = fns.mapNotNull { it.toGetter() }

        // convert the getters/setters into vals and vars.
        // * vals can only have a getter
        // * vars need a getter AND setter
        // There will be left over setters - they'll be converted to functions later
        val valsAndVars = getters.map { (getterName, getter) ->
          // check if the getter can be paired with a setter
          val matchedSetter = setters.mapNotNull { (setterName, setter) ->
            setter.takeIf { setterName == getterName }
          }

          when {
            matchedSetter.isEmpty() -> RdbCreateableElement.Val(
              name = getterName,
              getterFn = getter,
            )

            matchedSetter.size == 1 -> RdbCreateableElement.Var(
              name = getterName,
              getterFn = getter,
              setterFn = matchedSetter.first(),
            )

            else                    ->
              // this error shouldn't happen...
              error("too many setters (${matchedSetter.size}) for getter. getterName:$getterName, matchedSetter:${matchedSetter.map { it.name }}")
          }
        }

        val (staticFns, memberFns) = fns.filter { fn ->
          // filter all functions that were not converted into vals/vars
          fn.name !in valsAndVars.filterIsInstance<RdbCreateableElement.Prop>()
            .flatMap { it.fnNames }
        }.map { otherFn ->
          val baseName = "rocksdb_${creatable.name}_"
          RdbCreateableElement.Fn(
            name = otherFn.name.substringAfter(baseName),
            fn = otherFn,
            owner = creatable,
          )
        }.partition {
          // some functions might not have creatable as a parameter - they are either
          // static functions, or constructors
          it.static
        }

        val (constructorFns, staticUtilFns) = staticFns.partition { fn ->
          // constructor functions do not have the creatable as a parameter, and will return
          // the creatable
          fn.fn.valueParameters.none { it.sameTypeAs(creatable.type) }
              && fn.fn.returnType.kotlinType(nullable = false) == creatable.type.kotlinType(nullable = false)
        }

        // convert vals, vars, and functions to Kotlin source code
        val elements = (memberFns + valsAndVars).joinToString("\n\n") { element ->
          when (element) {
            is RdbCreateableElement.Fn  ->
              element.createKotlinFn().prependIndent("  ")

            is RdbCreateableElement.Val ->
              """
              |  val ${element.prettyName}: ${element.returnTypeClassifier.kotlinType()}
              |    get() = ${element.getterFn.name}(${creatable.propName})${element.returnTypeClassifier.getterConverter()}
            """.trimMargin()

            is RdbCreateableElement.Var -> {

              val setterConverter = when {
                element.returnTypeClassifier.kotlinType() == "Boolean" -> when {
                  // sometimes booleans are ints? https://github.com/facebook/rocksdb/issues/11080
                  element.setterFn.valueParameters[1].type.classifier.kotlinType() == "Int" -> ".toInt()"
                  else                                                                      -> element.returnTypeClassifier.setterConverter()
                }

                else                                                   -> ""
              }

              """
                |  var ${element.prettyName}: ${element.returnTypeClassifier.kotlinType()}
                |    get() = ${element.getterFn.name}(${creatable.propName})${element.returnTypeClassifier.getterConverter()}
                |    set(value) = ${element.setterFn.name}(${creatable.propName}, value$setterConverter)
              """.trimMargin()
            }
          }
        }

        val noArgCreateFn =
          constructorFns.firstOrNull { it.fn.valueParameters.isEmpty() }?.let { fn ->
            """ = ${fn.fn.name}() ?: error("could not instantiate new ${creatable.prettyName}")"""
          } ?: ""
        val constructors = constructorFns.filter {
          it.fn.valueParameters.isNotEmpty()
        }.joinToString("\n\n") {
          it.createKotlinConstructor()
        }.prependIndent("  ")

        val companionObject = if (staticUtilFns.isEmpty()) {
          ""
        } else {
          """
            |companion object {
            |${staticUtilFns.joinToString("\n\n") { it.createKotlinFn() }.prependIndent("  ")}
            |}
          """.trimMargin()
        }.prependIndent("  ")

        val classContent = listOf(
          """
            |override fun getPointer(scope: AutofreeScope): CPointer<${creatable.coreKotlinType}> =
            |  ${creatable.propName}.getPointer(scope)
          """.trimMargin().prependIndent("  "),
          constructors,
          elements,
          companionObject,
        ).filter { it.isNotBlank() }
          .joinToString(separator = "\n\n")

        creatable.prettyName to """
          |package dev.adamko.kotlin.on.the.rocksdb
          |
          |import org.rocksdb.*
          |import kotlinx.cinterop.*
          |
          |class ${creatable.prettyName}(
          |  private val ${creatable.propName}: ${creatable.type.kotlinType(nullable = false)}$noArgCreateFn
          |) : CValuesRef<${creatable.coreKotlinType}>() {
          |
          |$classContent
          |}
          |
        """.trimMargin()
      }
  }
}

private fun KmType.isUnit() =
  classifier.let { it is KmClassifier.Class && it.name == "kotlin/Unit" }


private fun KmClassifier.kotlinType(): String {
  return when (this) {
    is KmClassifier.Class         -> {

      when {
        // the RDB C API uses `unsigned char` for booleans
        name == "kotlin/UByte"          -> "Boolean"
        name.startsWith("org/rocksdb/") -> name.substringAfter("org/rocksdb/")
        else                            -> name.substringAfter("kotlin/")
      }
    }

    is KmClassifier.TypeAlias     -> name

    is KmClassifier.TypeParameter -> this.toString()
  }
}

private fun KmType.kotlinType(nullable: Boolean? = null, convertable: Boolean = true): String {

  val typeParams = this.arguments.map { typeParam ->
    typeParam.type?.kotlinType(convertable = false) ?: "*"
  }.joinToString(", ").let {
    if (it.isBlank()) it else "<$it>"
  }

  val nullableMarker = when {
    nullable == false                                -> ""
    nullable == true || Flag.Type.IS_NULLABLE(flags) -> "?"
    else                                             -> ""
  }

  val kotlinType = when (val classifier = this.classifier) {
    is KmClassifier.Class         -> {
      when {
        convertable && classifier.name == "kotlin/UByte" ->
          "Boolean"

        else                                             ->
          classifier.name.substringAfterLast('/') + typeParams
      }
    }

    is KmClassifier.TypeAlias     -> classifier.name + typeParams

    is KmClassifier.TypeParameter -> this.toString()
  }

  return when (kotlinType) {
    // use K/N cinterop typealias helpers
    "CPointer<CPointed>" -> "COpaquePointer"
    "ByteVarOf<Byte>"    -> "ByteVar"
    else                 -> kotlinType
  } + nullableMarker
}

private fun KmClassifier.getterConverter(): String = when (this.kotlinType()) {
  "Boolean" -> ".toBoolean()"
  else      -> ""
}

private fun KmClassifier.setterConverter(): String = when (this.kotlinType()) {
  "Boolean" -> ".toUByte()"
  else      -> ""
}

private class RdbCreateable(
  val name: String,
  createFn: KmFunction,
) {
  val createFnName: String = createFn.name
  val prettyName: String = createableNameMap[name] ?: error("missing pretty name for $name")
  val type: KmType = createFn.returnType
  val coreKotlinType: String = type.arguments.first().type!!.kotlinType(nullable = false)
  val propName: String = prettyName.lowercaseFirstChar()

  val createFn = RdbCreateableElement.Fn(
    fn = createFn,
    name = createFnName,
    owner = this,
  )
}

/** Create Kotlin source code for a function */
private fun RdbCreateableElement.Fn.createKotlinFn(): String {

  val filteredParams = if (fn.valueParameters.firstOrNull().sameTypeAs(owner.type)) {
    // drop the 'owner' param, because the property is named differently to the function arg
    fn.valueParameters.drop(1)
  } else {
    fn.valueParameters
  }

  val params = filteredParams.map(RdbCreateableElement.Fn::Param)

  val fnArgs = params.joinToString("\n") { it.paramString }
  val fnArgNames = params.joinToString(", ") {
    it.prettyName + it.converter
  }

  val nullable = if (static) false else null
  val elvis = if (static) """${'\n'} ?: error("could not execute '${fn.name}'")""" else ""

  return if (fnArgs.isBlank()) {
    """
      |fun $prettyName(): ${fn.returnType.kotlinType(nullable = nullable)} = 
      |  ${fn.name}(${if (!static) owner.propName else ""})${fn.returnType.classifier.getterConverter()} $elvis
    """.trimMargin()
  } else {
    """
      |fun $prettyName(
      |${fnArgs.prependIndent("  ")}
      |): ${fn.returnType.kotlinType(nullable = nullable)} = 
      |  ${fn.name}(${if (!static) "${owner.propName}, " else ""}$fnArgNames)${fn.returnType.classifier.getterConverter()} $elvis
    """.trimMargin()
  }
}

private fun RdbCreateableElement.Fn.createKotlinConstructor(): String {

  val params = fn.valueParameters.map(RdbCreateableElement.Fn::Param)

  val fnArgs = params.joinToString("\n") { it.paramString }
  val fnArgNames = params.joinToString(", ") {
    it.prettyName + it.converter
  }

  val constructorArgs = if (fnArgs.isEmpty()) "" else
    "\n${fnArgs.prependIndent("  ")}\n"

  return """
      |constructor($constructorArgs): this(${fn.name}($fnArgNames) ?: error("could not instantiate new ${owner.prettyName}"))
    """.trimMargin()
}


private fun KmValueParameter?.sameTypeAs(otherType: KmType): Boolean {
  if (this == null) return false

  return type.arguments.firstOrNull()?.type?.kotlinType() == otherType.arguments.firstOrNull()?.type?.kotlinType()
}


private fun RdbCreateable.cpointerType() =
  "CPointer<${type.classifier.kotlinType().replace('/', '.')}>"

/** The  */
private sealed interface RdbCreateableElement {
  val name: String

  sealed interface Prop : RdbCreateableElement {
    val fnNames: List<String>
  }

  data class Val(
    override val name: String,
    val getterFn: KmFunction,
  ) : Prop {
    override val fnNames: List<String> = listOf(getterFn.name)
  }

  data class Var(
    override val name: String,
    val setterFn: KmFunction,
    val getterFn: KmFunction,
  ) : Prop {
    override val fnNames: List<String> = listOf(getterFn.name, setterFn.name)
  }

  data class Fn(
    override val name: String,
    val fn: KmFunction,
    val owner: RdbCreateable,
  ) : RdbCreateableElement {
    val static = fn.valueParameters.none { it.sameTypeAs(owner.type) }

    data class Param(
      val actual: KmValueParameter,
    ) {
      val prettyName = actual.name.knmNameToPrettyName()
      val kotlinType = actual.type.kotlinType()
      val converter = actual.type.classifier.setterConverter()
      val paramString = "${prettyName}: ${kotlinType}, "
    }
  }
}

private val RdbCreateableElement.returnTypeClassifier
  get() = when (this) {
    is RdbCreateableElement.Val -> getterFn.returnType.classifier
    is RdbCreateableElement.Var -> getterFn.returnType.classifier
    is RdbCreateableElement.Fn  -> fn.returnType.classifier
  }


private val RdbCreateableElement.prettyName: String
  get() = name.knmNameToPrettyName()

/** Prettify the funciton names */
private fun String.knmNameToPrettyName() =
  // rename similar functions, so functions are overloaded
  replace("put_cf", "put")
    .replace("putv_cf", "put")
    .replace("merge_cf", "merge")
    .replace("delete_cf", "delete")
    .replace("deletev_cf", "delete")
    .replace("range_cf", "range")
    .split("_")
    .joinToString("") {
      when (it.lowercase()) {
        "mb"                      -> it // keep MB/Mb casing, because Mb != MB
        "readahead"               -> "ReadAhead"
        "subcompactions"          -> "SubCompactions"
        "transactiondb"           -> "TransactionDb"
        "writebatch"              -> "WriteBatch"
        "perfcontext"             -> "PerfContext"
        "compactionfilter"        -> "CompactionFilter"
        "compactionfilterfactory" -> "CompactionFilterFactory"
        "optimistictransaction"   -> "OptimisticTransaction"
        "compactionfiltercontext" -> "CompactionFilterContext"
        "compactoptions"          -> "CompactOptions"
        "mempurge"                -> "MemPurge"
        "val"                     -> "Value"
        "klen", "keylen"          -> "KeyLength"
        "vlen", "vallen"          -> "ValueLength"
        "tslen"                   -> "tsLength"
        "putv", "putcf"           -> "Put"
        "mergev", "mergecf"       -> "Merge"
        "rangev", "rangecf"       -> "Range"
        "deletev", "deletecf"     -> "Delete"
        "singledelete"            -> "SingleDelete"
        "deleterange"             -> "DeleteRange"
        "errptr"                  -> "errorPointer"
        else                      -> it.lowercase().uppercaseFirstChar()
      }
    }.lowercaseFirstChar()

//data class RdbOptions(
//  val name: String,
//  val fn: KmFunction,
//) {
//  val fnName: String = fn.name
//  val prettyName: String = createableNameMap[name] ?: error("missing pretty name for $name")
//}

/** A map of all known [RdbCreateable] names, along with a pretty name */
private val createableNameMap = mapOf(
  "approximate_memory_usage" to "ApproximateMemoryUsage",
  "backup_engine_options" to "BackupEngineOptions",
  "block_based_options" to "BlockBasedOptions",
  "checkpoint" to "Checkpoint",
  "checkpoint_object" to "CheckpointObject",
  "compactionfilter" to "CompactionFilter",
  "compactionfilterfactory" to "CompactionFilterFactory",
  "compactoptions" to "CompactOptions",
  "comparator" to "Comparator",
  "comparator_with_ts" to "ComparatorWithTs",
  "cuckoo_options" to "CuckooOptions",
  "dbpath" to "DbPath",
  "envoptions" to "EnvOptions",
  "fifo_compaction_options" to "FifoCompactionOptions",
  "flushoptions" to "FlushOptions",
  "ingestexternalfileoptions" to "IngestExternalFileOptions",
  "jemalloc_nodump_allocator" to "JemallocNoDumpAllocator",
  "lru_cache_options" to "LruCacheOptions",
  "memory_consumers" to "MemoryConsumers",
  "mergeoperator" to "MergeOperator",
  "optimistictransaction_options" to "OptimisticTransactionOptions",
  "optimistictransactiondb_checkpoint_object" to "OptimisticTransactionDbCheckpointObject",
  "options" to "RocksDbOptions",
  "perfcontext" to "PerfContext",
  "ratelimiter" to "RateLimiter",
  "readoptions" to "ReadOptions",
  "restore_options" to "RestoreOptions",
  "slicetransform" to "SliceTransform",
  "sstfilewriter" to "SstFileWriter",
  "transaction_options" to "TransactionOptions",
  "transactiondb_checkpoint_object" to "TransactionDbCheckpointObject",
  "transactiondb_options" to "TransactionDbOptions",
  "universal_compaction_options" to "UniversalCompactionOptions",
  "writebatch" to "WriteBatch",
  "writebatch_wi" to "WriteBatchWi",
  "writeoptions" to "WriteOptions",
  "seqno" to "SeqNo",
)

private fun readLibraryMetadata(libraryPath: File): KlibModuleMetadata {
  check(libraryPath.exists()) { "Library does not exist: $libraryPath" }

  val libraryKonanFile = KonanFile(libraryPath.absolutePath)
  val library = resolveSingleFileKlib(
    libraryKonanFile,
    DummyLogger,
    ToolingSingleFileKlibResolveStrategy
  )

  return KlibModuleMetadata.read(
    object : KlibModuleMetadata.MetadataLibraryProvider {
      override val moduleHeaderData: ByteArray =
        library.moduleHeaderData

      override fun packageMetadata(fqName: String, partName: String): ByteArray =
        library.packageMetadata(fqName, partName)

      override fun packageMetadataParts(fqName: String): Set<String> =
        library.packageMetadataParts(fqName)
    }
  )
}
