package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class TransactionOptions(
  private val transactionOptions: CPointer<rocksdb_transaction_options_t> = rocksdb_transaction_options_create() ?: error("could not instantiate new TransactionOptions")
) : CValuesRef<rocksdb_transaction_options_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_transaction_options_t> =
    transactionOptions.getPointer(scope)

  fun destroy(): Unit = 
    rocksdb_transaction_options_destroy(transactionOptions) 

  fun setSetSnapshot(
    v: Boolean, 
  ): Unit = 
    rocksdb_transaction_options_set_set_snapshot(transactionOptions, v.toUByte()) 

  fun setDeadlockDetect(
    v: Boolean, 
  ): Unit = 
    rocksdb_transaction_options_set_deadlock_detect(transactionOptions, v.toUByte()) 

  fun setLockTimeout(
    lockTimeout: Long, 
  ): Unit = 
    rocksdb_transaction_options_set_lock_timeout(transactionOptions, lockTimeout) 

  fun setExpiration(
    expiration: Long, 
  ): Unit = 
    rocksdb_transaction_options_set_expiration(transactionOptions, expiration) 

  fun setDeadlockDetectDepth(
    depth: Long, 
  ): Unit = 
    rocksdb_transaction_options_set_deadlock_detect_depth(transactionOptions, depth) 

  fun setMaxWriteBatchSize(
    size: ULong, 
  ): Unit = 
    rocksdb_transaction_options_set_max_write_batch_size(transactionOptions, size) 

  fun setSkipPrepare(
    v: Boolean, 
  ): Unit = 
    rocksdb_transaction_options_set_skip_prepare(transactionOptions, v.toUByte()) 
}
