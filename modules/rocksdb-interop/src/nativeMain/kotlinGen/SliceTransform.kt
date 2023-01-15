package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class SliceTransform(
  private val sliceTransform: CPointer<rocksdb_slicetransform_t> = rocksdb_slicetransform_create_noop() ?: error("could not instantiate new SliceTransform")
) : CValuesRef<rocksdb_slicetransform_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_slicetransform_t> =
    sliceTransform.getPointer(scope)

  constructor(
    state: CValuesRef<*>?, 
    destructor: CPointer<CFunction<Function1<COpaquePointer?, Unit>>>?, 
    transform: CPointer<CFunction<Function4<COpaquePointer?, CPointer<ByteVar>?, ULong, CPointer<ULongVarOf<ULong>>?, CPointer<ByteVar>?>>>?, 
    inDomain: CPointer<CFunction<Function3<COpaquePointer?, CPointer<ByteVar>?, ULong, UByte>>>?, 
    inRange: CPointer<CFunction<Function3<COpaquePointer?, CPointer<ByteVar>?, ULong, UByte>>>?, 
    name: CPointer<CFunction<Function1<COpaquePointer?, CPointer<ByteVar>?>>>?, 
  ): this(rocksdb_slicetransform_create(state, destructor, transform, inDomain, inRange, name) ?: error("could not instantiate new SliceTransform"))
  
  constructor(
    arg0: ULong, 
  ): this(rocksdb_slicetransform_create_fixed_prefix(arg0) ?: error("could not instantiate new SliceTransform"))

  fun destroy(): Unit = 
    rocksdb_slicetransform_destroy(sliceTransform) 
}
