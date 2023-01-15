package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class CheckpointObject(
  private val checkpointObject: CPointer<rocksdb_checkpoint_t>
) : CValuesRef<rocksdb_checkpoint_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_checkpoint_t> =
    checkpointObject.getPointer(scope)

  constructor(
    db: CValuesRef<rocksdb_t>?, 
    errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
  ): this(rocksdb_checkpoint_object_create(db, errorPointer) ?: error("could not instantiate new CheckpointObject"))

  fun destroy(): Unit = 
    rocksdb_checkpoint_object_destroy(checkpointObject) 
}
