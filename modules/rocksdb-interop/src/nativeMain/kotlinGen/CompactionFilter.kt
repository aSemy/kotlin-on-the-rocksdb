package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class CompactionFilter(
  private val compactionFilter: CPointer<rocksdb_compactionfilter_t>
) : CValuesRef<rocksdb_compactionfilter_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_compactionfilter_t> =
    compactionFilter.getPointer(scope)

  constructor(
    state: CValuesRef<*>?,
    destructor: CPointer<CFunction<Function1<COpaquePointer?, Unit>>>?,
    filter: CPointer<CFunction<Function9<COpaquePointer?, Int, CPointer<ByteVar>?, ULong, CPointer<ByteVar>?, ULong, CPointer<CPointerVarOf<CPointer<ByteVar>>>?, CPointer<ULongVarOf<ULong>>?, CPointer<UByteVarOf<UByte>>?, UByte>>>?,
    name: CPointer<CFunction<Function1<COpaquePointer?, CPointer<ByteVar>?>>>?,
  ): this(rocksdb_compactionfilter_create(state, destructor, filter, name) ?: error("could not instantiate new CompactionFilter"))

  fun setIgnoreSnapshots(
    arg1: Boolean,
  ): Unit =
    rocksdb_compactionfilter_set_ignore_snapshots(compactionFilter, arg1.toUByte())

  fun destroy(): Unit =
    rocksdb_compactionfilter_destroy(compactionFilter)
}
