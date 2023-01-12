package buildsrc.knparse

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

  fun processFeatureContext(library: File): String {
    val metadata = readLibraryMetadata(library)

    val functions = metadata.fragments.flatMap { it.pkg?.functions ?: emptyList() }

    val creatables = functions
      .filter { it.name.endsWith("_create") && it.returnType.classifier.kotlinType() != "Unit" } // && it.valueParameters.isEmpty() }
      .map { fn ->
        RdbCreateable(
          fn.name.substringAfter("rocksdb_").substringBefore("_create"),
          fn,
        )
      }

    return creatables
      .associateWith { creatable ->
        functions.filter { fn ->
          fn.name.startsWith("rocksdb_${creatable.name}_") &&
              (fn.valueParameters.any { it.sameTypeAs(creatable.type) }
                  ||
                  fn.returnType.kotlinType() == creatable.type.kotlinType()
                  )
        }
      }.mapValues { (creatable, fns) ->

        val setterFnBaseName = "rocksdb_${creatable.name}_set_"
        val getterFnBaseName = "rocksdb_${creatable.name}_get_"

        fun KmFunction.toSetter(): Pair<String, KmFunction>? {
          val isSetter = name.startsWith(setterFnBaseName)
              && valueParameters.size == 2
              && valueParameters.first().sameTypeAs(creatable.type)
              && returnType.isUnit()

          return if (!isSetter) null else {
            name.substringAfter(setterFnBaseName) to this
          }
        }

        fun KmFunction.toGetter(): Pair<String, KmFunction>? {
          val isGetter = name.startsWith(getterFnBaseName)
              && valueParameters.size == 1
              && valueParameters.first().sameTypeAs(creatable.type)

          return if (!isGetter) null else {
            name.substringAfter(getterFnBaseName) to this
          }
        }

        val setters = fns.mapNotNull { it.toSetter() }
        val getters = fns.mapNotNull { it.toGetter() }

        val valsAndVars = getters.map { (gName, getter) ->
          val matchedSetter = setters.mapNotNull { (sName, setter) ->
            setter.takeIf { sName == gName }
          }

          when {
            matchedSetter.isEmpty() -> RdbCreateableElement.Val(
              name = gName,
              getterFn = getter,
            )

            matchedSetter.size == 1 -> RdbCreateableElement.Var(
              name = gName,
              getterFn = getter,
              setterFn = matchedSetter.first(),
            )

            else                    -> error("too many setters (${matchedSetter.size}) for getter: gName:$gName, matchedSetter:${matchedSetter.map { it.name }}")
          }
        }

        val otherFns = fns.filter { fn ->
          fn.name !in valsAndVars.filterIsInstance<RdbCreateableElement.Prop>()
            .flatMap { it.fnNames }
        }

        val (staticFns, memberFns) = otherFns.map { otherFn ->
          val baseName = "rocksdb_${creatable.name}_"
          RdbCreateableElement.Fn(
            name = otherFn.name.substringAfter(baseName),
            fn = otherFn,
            owner = creatable,
          )
        }.partition { it.static }

        println("${creatable.name} - staticFns [${staticFns.size}]: ${staticFns.map { it.name }}")
        println("${creatable.name} - memberFns [${memberFns.size}]: ${memberFns.map { it.name }}")

        val elements = (memberFns + valsAndVars).joinToString("\n\n") { vv ->
          when (vv) {
            is RdbCreateableElement.Fn  ->
              vv.createKotlinFn().prependIndent("  ")

            is RdbCreateableElement.Val ->
              """
              |  val ${vv.prettyName}: ${vv.returnTypeClassifier.kotlinType()}
              |    get() = ${vv.getterFn.name}(${creatable.propName})${vv.returnTypeClassifier.getterConverter()}
            """.trimMargin()

            is RdbCreateableElement.Var -> {

              val setterConverter = when {
                vv.returnTypeClassifier.kotlinType() == "Boolean" -> when {
                  vv.setterFn.valueParameters[1].type.classifier.kotlinType() == "Int" -> ".toInt()"
                  else                                                                 -> vv.returnTypeClassifier.setterConverter()
                }

                else                                              -> ""
              }

              """
                |  var ${vv.prettyName}: ${vv.returnTypeClassifier.kotlinType()}
                |    get() = ${vv.getterFn.name}(${creatable.propName})${vv.returnTypeClassifier.getterConverter()}
                |    set(value) = ${vv.setterFn.name}(${creatable.propName}, value$setterConverter)
              """.trimMargin()
            }
          }
        }

        """
          |
          |class ${creatable.prettyName}(
          |  private val ${creatable.propName}: ${creatable.type.kotlinType()}
          |) {
          |$elements
          |
          |  companion object {
          |${staticFns.joinToString("\n\n") { it.createKotlinFn() }.prependIndent("    ")}
          |  }
          |}
          |
        """.trimMargin()

      }.values.joinToString(
        "",
        prefix = """
          |package dev.adamko.kotlin.on.the.rocksdb
          |
          |import org.rocksdb.*
          |import kotlinx.cinterop.*
          |
        """.trimMargin()
      )
  }
}

private fun KmType.isUnit() =
  classifier.let { it is KmClassifier.Class && it.name == "kotlin/Unit" }


private fun KmClassifier.kotlinType(): String {
  return when (this) {
    is KmClassifier.Class         -> {

      when {
        name == "kotlin/UByte"             -> "Boolean"
        name.startsWith("cnames/structs/") -> name.substringAfter("cnames/structs/")
        else                               -> name.substringAfter("kotlin/")
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
        convertable && classifier.name == "kotlin/UByte" -> "Boolean"
        else                                             -> classifier.name.substringAfterLast('/') + typeParams
      }
    }

    is KmClassifier.TypeAlias     -> classifier.name + typeParams

    is KmClassifier.TypeParameter -> this.toString()
  }

  return when (kotlinType) {
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
  val propName: String = prettyName.lowercaseFirstChar()

  val createFn = RdbCreateableElement.Fn(
    fn = createFn,
    name = createFnName,
    owner = this,
  )
}

private fun RdbCreateableElement.Fn.createKotlinFn(): String {

  val filteredParams = if (fn.valueParameters.firstOrNull().sameTypeAs(owner.type)) {
    fn.valueParameters.drop(1)
  } else {
    fn.valueParameters
  }

  data class Param(
    val actual: KmValueParameter,
  ) {
    val prettyName = actual.name.knmNameToPrettyName()
    val kotlinType = actual.type.kotlinType()
    val converter = actual.type.classifier.setterConverter()
    val paramString = "${prettyName}: ${kotlinType}, "
  }

  val params = filteredParams.map(::Param)

  val fnArgs = params.joinToString("\n") { it.paramString }
  val fnArgNames = params.joinToString(", ") {
    it.prettyName + it.converter
  }

  val nullable = if (static) false else null
  val elvis = if (static) """${'\n'}  ?: error("could not execute '${fn.name}'")""" else ""

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


private fun KmValueParameter?.sameTypeAs(otherType: KmType): Boolean {
  if (this == null) return false

  return type.arguments.firstOrNull()?.type?.kotlinType() == otherType.arguments.firstOrNull()?.type?.kotlinType()

}


private fun RdbCreateable.cpointerType() =
  "CPointer<${type.classifier.kotlinType().replace('/', '.')}>"

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

private fun String.knmNameToPrettyName() =
  replace("put_cf", "put")
    .replace("putv_cf", "put")
    .replace("merge_cf", "merge")
    .replace("delete_cf", "delete")
    .replace("range_cf", "range")
    .split("_")
    .joinToString("") {
      when (it.lowercase()) {
        "mb"                      -> it
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
