package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class RestoreOptions(
  private val restoreOptions: CPointer<rocksdb_restore_options_t> = rocksdb_restore_options_create() ?: error("could not instantiate new RestoreOptions")
) : CValuesRef<rocksdb_restore_options_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_restore_options_t> =
    restoreOptions.getPointer(scope)

  fun destroy(): Unit = 
    rocksdb_restore_options_destroy(restoreOptions) 

  fun setKeepLogFiles(
    v: Int, 
  ): Unit = 
    rocksdb_restore_options_set_keep_log_files(restoreOptions, v) 
}
