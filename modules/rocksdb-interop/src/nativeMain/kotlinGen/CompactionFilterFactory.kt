package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class CompactionFilterFactory(
  private val compactionFilterFactory: CPointer<rocksdb_compactionfilterfactory_t>
) : CValuesRef<rocksdb_compactionfilterfactory_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_compactionfilterfactory_t> =
    compactionFilterFactory.getPointer(scope)

  constructor(
    state: CValuesRef<*>?,
    destructor: CPointer<CFunction<Function1<COpaquePointer?, Unit>>>?,
    createCompactionFilter: CPointer<CFunction<Function2<COpaquePointer?, CPointer<rocksdb_compactionfiltercontext_t>?, CPointer<rocksdb_compactionfilter_t>?>>>?,
    name: CPointer<CFunction<Function1<COpaquePointer?, CPointer<ByteVar>?>>>?,
  ): this(
    rocksdb_compactionfilterfactory_create(state, destructor, createCompactionFilter, name) 
      ?: error("could not instantiate new CompactionFilterFactory")
  )

  fun destroy(): Unit =
    rocksdb_compactionfilterfactory_destroy(compactionFilterFactory)
}
