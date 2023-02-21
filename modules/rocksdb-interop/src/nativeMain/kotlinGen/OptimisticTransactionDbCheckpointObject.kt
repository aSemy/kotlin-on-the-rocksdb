package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class OptimisticTransactionDbCheckpointObject(
  private val optimisticTransactionDbCheckpointObject: CPointer<rocksdb_checkpoint_t>
) : CValuesRef<rocksdb_checkpoint_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_checkpoint_t> =
    optimisticTransactionDbCheckpointObject.getPointer(scope)

  constructor(
    otxnDb: CValuesRef<rocksdb_optimistictransactiondb_t>?,
    errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?,
  ): this(
    rocksdb_optimistictransactiondb_checkpoint_object_create(otxnDb, errorPointer) 
      ?: error("could not instantiate new OptimisticTransactionDbCheckpointObject")
  )
}
