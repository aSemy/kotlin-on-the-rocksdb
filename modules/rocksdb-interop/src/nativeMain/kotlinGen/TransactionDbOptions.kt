package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class TransactionDbOptions(
  private val transactionDbOptions: CPointer<rocksdb_transactiondb_options_t> = rocksdb_transactiondb_options_create() ?: error("could not instantiate new TransactionDbOptions")
) : CValuesRef<rocksdb_transactiondb_options_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_transactiondb_options_t> =
    transactionDbOptions.getPointer(scope)

  fun destroy(): Unit = 
    rocksdb_transactiondb_options_destroy(transactionDbOptions) 

  fun setMaxNumLocks(
    maxNumLocks: Long, 
  ): Unit = 
    rocksdb_transactiondb_options_set_max_num_locks(transactionDbOptions, maxNumLocks) 

  fun setNumStripes(
    numStripes: ULong, 
  ): Unit = 
    rocksdb_transactiondb_options_set_num_stripes(transactionDbOptions, numStripes) 

  fun setTransactionLockTimeout(
    txnLockTimeout: Long, 
  ): Unit = 
    rocksdb_transactiondb_options_set_transaction_lock_timeout(transactionDbOptions, txnLockTimeout) 

  fun setDefaultLockTimeout(
    defaultLockTimeout: Long, 
  ): Unit = 
    rocksdb_transactiondb_options_set_default_lock_timeout(transactionDbOptions, defaultLockTimeout) 
}
