package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class TransactionDbCheckpointObject(
  private val transactionDbCheckpointObject: CPointer<rocksdb_checkpoint_t>
) : CValuesRef<rocksdb_checkpoint_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_checkpoint_t> =
    transactionDbCheckpointObject.getPointer(scope)

  constructor(
    txnDb: CValuesRef<rocksdb_transactiondb_t>?,
    errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?,
  ): this(rocksdb_transactiondb_checkpoint_object_create(txnDb, errorPointer) ?: error("could not instantiate new TransactionDbCheckpointObject"))
}
