package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class BackupEngineOptions(
  private val backupEngineOptions: CPointer<rocksdb_backup_engine_options_t>
) : CValuesRef<rocksdb_backup_engine_options_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_backup_engine_options_t> =
    backupEngineOptions.getPointer(scope)

  constructor(
    backupDir: String?, 
  ): this(rocksdb_backup_engine_options_create(backupDir) ?: error("could not instantiate new BackupEngineOptions"))

  fun setBackupDir(
    backupDir: String?, 
  ): Unit = 
    rocksdb_backup_engine_options_set_backup_dir(backupEngineOptions, backupDir) 

  fun setEnv(
    env: CValuesRef<rocksdb_env_t>?, 
  ): Unit = 
    rocksdb_backup_engine_options_set_env(backupEngineOptions, env) 

  fun destroy(): Unit = 
    rocksdb_backup_engine_options_destroy(backupEngineOptions) 

  var shareTableFiles: Boolean
    get() = rocksdb_backup_engine_options_get_share_table_files(backupEngineOptions).toBoolean()
    set(value) = rocksdb_backup_engine_options_set_share_table_files(backupEngineOptions, value.toUByte())

  var sync: Boolean
    get() = rocksdb_backup_engine_options_get_sync(backupEngineOptions).toBoolean()
    set(value) = rocksdb_backup_engine_options_set_sync(backupEngineOptions, value.toUByte())

  var destroyOldData: Boolean
    get() = rocksdb_backup_engine_options_get_destroy_old_data(backupEngineOptions).toBoolean()
    set(value) = rocksdb_backup_engine_options_set_destroy_old_data(backupEngineOptions, value.toUByte())

  var backupLogFiles: Boolean
    get() = rocksdb_backup_engine_options_get_backup_log_files(backupEngineOptions).toBoolean()
    set(value) = rocksdb_backup_engine_options_set_backup_log_files(backupEngineOptions, value.toUByte())

  var backupRateLimit: ULong
    get() = rocksdb_backup_engine_options_get_backup_rate_limit(backupEngineOptions)
    set(value) = rocksdb_backup_engine_options_set_backup_rate_limit(backupEngineOptions, value)

  var restoreRateLimit: ULong
    get() = rocksdb_backup_engine_options_get_restore_rate_limit(backupEngineOptions)
    set(value) = rocksdb_backup_engine_options_set_restore_rate_limit(backupEngineOptions, value)

  var maxBackgroundOperations: Int
    get() = rocksdb_backup_engine_options_get_max_background_operations(backupEngineOptions)
    set(value) = rocksdb_backup_engine_options_set_max_background_operations(backupEngineOptions, value)

  var callbackTriggerIntervalSize: ULong
    get() = rocksdb_backup_engine_options_get_callback_trigger_interval_size(backupEngineOptions)
    set(value) = rocksdb_backup_engine_options_set_callback_trigger_interval_size(backupEngineOptions, value)

  var maxValidBackupsToOpen: Int
    get() = rocksdb_backup_engine_options_get_max_valid_backups_to_open(backupEngineOptions)
    set(value) = rocksdb_backup_engine_options_set_max_valid_backups_to_open(backupEngineOptions, value)

  var shareFilesWithChecksumNaming: Int
    get() = rocksdb_backup_engine_options_get_share_files_with_checksum_naming(backupEngineOptions)
    set(value) = rocksdb_backup_engine_options_set_share_files_with_checksum_naming(backupEngineOptions, value)
}
