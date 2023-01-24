package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class MergeOperator(
  private val mergeOperator: CPointer<rocksdb_mergeoperator_t>
) : CValuesRef<rocksdb_mergeoperator_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_mergeoperator_t> =
    mergeOperator.getPointer(scope)

  constructor(
    state: CValuesRef<*>?,
    destructor: CPointer<CFunction<Function1<COpaquePointer?, Unit>>>?,
    fullMerge: CPointer<CFunction<Function10<COpaquePointer?, CPointer<ByteVar>?, ULong, CPointer<ByteVar>?, ULong, CPointer<CPointerVarOf<CPointer<ByteVar>>>?, CPointer<ULongVarOf<ULong>>?, Int, CPointer<UByteVarOf<UByte>>?, CPointer<ULongVarOf<ULong>>?, CPointer<ByteVar>?>>>?,
    partialMerge: CPointer<CFunction<Function8<COpaquePointer?, CPointer<ByteVar>?, ULong, CPointer<CPointerVarOf<CPointer<ByteVar>>>?, CPointer<ULongVarOf<ULong>>?, Int, CPointer<UByteVarOf<UByte>>?, CPointer<ULongVarOf<ULong>>?, CPointer<ByteVar>?>>>?,
    deleteValue: CPointer<CFunction<Function3<COpaquePointer?, CPointer<ByteVar>?, ULong, Unit>>>?,
    name: CPointer<CFunction<Function1<COpaquePointer?, CPointer<ByteVar>?>>>?,
  ): this(rocksdb_mergeoperator_create(state, destructor, fullMerge, partialMerge, deleteValue, name) ?: error("could not instantiate new MergeOperator"))

  fun destroy(): Unit =
    rocksdb_mergeoperator_destroy(mergeOperator)
}
