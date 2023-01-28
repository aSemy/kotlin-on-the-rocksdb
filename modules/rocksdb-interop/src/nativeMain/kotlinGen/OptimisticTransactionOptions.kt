package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class OptimisticTransactionOptions(
  private val optimisticTransactionOptions: CPointer<rocksdb_optimistictransaction_options_t> = rocksdb_optimistictransaction_options_create() 
     ?: error("could not instantiate new OptimisticTransactionOptions")
) : CValuesRef<rocksdb_optimistictransaction_options_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_optimistictransaction_options_t> =
    optimisticTransactionOptions.getPointer(scope)

  fun destroy(): Unit =
    rocksdb_optimistictransaction_options_destroy(optimisticTransactionOptions)

  fun setSetSnapshot(
    v: Boolean,
  ): Unit =
    rocksdb_optimistictransaction_options_set_set_snapshot(optimisticTransactionOptions, v.toUByte())
}
