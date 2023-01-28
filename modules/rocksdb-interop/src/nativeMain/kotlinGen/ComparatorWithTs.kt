package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class ComparatorWithTs(
  private val comparatorWithTs: CPointer<rocksdb_comparator_t>
) : CValuesRef<rocksdb_comparator_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_comparator_t> =
    comparatorWithTs.getPointer(scope)

  constructor(
    state: CValuesRef<*>?,
    destructor: CPointer<CFunction<Function1<COpaquePointer?, Unit>>>?,
    compare: CPointer<CFunction<Function5<COpaquePointer?, CPointer<ByteVar>?, ULong, CPointer<ByteVar>?, ULong, Int>>>?,
    compareTs: CPointer<CFunction<Function5<COpaquePointer?, CPointer<ByteVar>?, ULong, CPointer<ByteVar>?, ULong, Int>>>?,
    compareWithoutTs: CPointer<CFunction<Function7<COpaquePointer?, CPointer<ByteVar>?, ULong, UByte, CPointer<ByteVar>?, ULong, UByte, Int>>>?,
    name: CPointer<CFunction<Function1<COpaquePointer?, CPointer<ByteVar>?>>>?,
    timestampSize: ULong,
  ): this(
    rocksdb_comparator_with_ts_create(state, destructor, compare, compareTs, compareWithoutTs, name, timestampSize) 
      ?: error("could not instantiate new ComparatorWithTs")
  )
}
