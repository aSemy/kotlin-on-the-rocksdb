package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class RestoreOptions(
  private val restoreOptions: CPointer<rocksdb_restore_options_t>?
) {
  fun destroy(): Unit = 
    rocksdb_restore_options_destroy(restoreOptions) 

  fun setKeepLogFiles(
    v: Int, 
  ): Unit = 
    rocksdb_restore_options_set_keep_log_files(restoreOptions, v) 

  companion object {
    fun create(): CPointer<rocksdb_restore_options_t> = 
      rocksdb_restore_options_create() 
       ?: error("could not execute 'rocksdb_restore_options_create'")
  }
}

class BackupEngineOptions(
  private val backupEngineOptions: CPointer<rocksdb_backup_engine_options_t>?
) {
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

  companion object {
    fun create(
      backupDir: String?, 
    ): CPointer<rocksdb_backup_engine_options_t> = 
      rocksdb_backup_engine_options_create(backupDir) 
       ?: error("could not execute 'rocksdb_backup_engine_options_create'")
  }
}

class CheckpointObject(
  private val checkpointObject: CPointer<rocksdb_checkpoint_t>?
) {
  fun destroy(): Unit = 
    rocksdb_checkpoint_object_destroy(checkpointObject) 

  companion object {
    fun create(
      db: CValuesRef<rocksdb_t>?, 
      errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
    ): CPointer<rocksdb_checkpoint_t> = 
      rocksdb_checkpoint_object_create(db, errorPointer) 
       ?: error("could not execute 'rocksdb_checkpoint_object_create'")
  }
}

class WriteBatch(
  private val writeBatch: CPointer<rocksdb_writebatch_t>?
) {
  fun destroy(): Unit = 
    rocksdb_writebatch_destroy(writeBatch) 

  fun clear(): Unit = 
    rocksdb_writebatch_clear(writeBatch) 

  fun count(): Int = 
    rocksdb_writebatch_count(writeBatch) 

  fun put(
    key: String?, 
    keyLength: ULong, 
    value: String?, 
    valueLength: ULong, 
  ): Unit = 
    rocksdb_writebatch_put(writeBatch, key, keyLength, value, valueLength) 

  fun put(
    columnFamily: CValuesRef<rocksdb_column_family_handle_t>?, 
    key: String?, 
    keyLength: ULong, 
    value: String?, 
    valueLength: ULong, 
  ): Unit = 
    rocksdb_writebatch_put_cf(writeBatch, columnFamily, key, keyLength, value, valueLength) 

  fun putWithTs(
    columnFamily: CValuesRef<rocksdb_column_family_handle_t>?, 
    key: String?, 
    keyLength: ULong, 
    ts: String?, 
    tsLength: ULong, 
    value: String?, 
    valueLength: ULong, 
  ): Unit = 
    rocksdb_writebatch_put_cf_with_ts(writeBatch, columnFamily, key, keyLength, ts, tsLength, value, valueLength) 

  fun put(
    numKeys: Int, 
    keysList: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
    keysListSizes: CValuesRef<ULongVarOf<ULong>>?, 
    numValues: Int, 
    valuesList: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
    valuesListSizes: CValuesRef<ULongVarOf<ULong>>?, 
  ): Unit = 
    rocksdb_writebatch_putv(writeBatch, numKeys, keysList, keysListSizes, numValues, valuesList, valuesListSizes) 

  fun put(
    columnFamily: CValuesRef<rocksdb_column_family_handle_t>?, 
    numKeys: Int, 
    keysList: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
    keysListSizes: CValuesRef<ULongVarOf<ULong>>?, 
    numValues: Int, 
    valuesList: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
    valuesListSizes: CValuesRef<ULongVarOf<ULong>>?, 
  ): Unit = 
    rocksdb_writebatch_putv_cf(writeBatch, columnFamily, numKeys, keysList, keysListSizes, numValues, valuesList, valuesListSizes) 

  fun merge(
    key: String?, 
    keyLength: ULong, 
    value: String?, 
    valueLength: ULong, 
  ): Unit = 
    rocksdb_writebatch_merge(writeBatch, key, keyLength, value, valueLength) 

  fun merge(
    columnFamily: CValuesRef<rocksdb_column_family_handle_t>?, 
    key: String?, 
    keyLength: ULong, 
    value: String?, 
    valueLength: ULong, 
  ): Unit = 
    rocksdb_writebatch_merge_cf(writeBatch, columnFamily, key, keyLength, value, valueLength) 

  fun merge(
    numKeys: Int, 
    keysList: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
    keysListSizes: CValuesRef<ULongVarOf<ULong>>?, 
    numValues: Int, 
    valuesList: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
    valuesListSizes: CValuesRef<ULongVarOf<ULong>>?, 
  ): Unit = 
    rocksdb_writebatch_mergev(writeBatch, numKeys, keysList, keysListSizes, numValues, valuesList, valuesListSizes) 

  fun mergeCf(
    columnFamily: CValuesRef<rocksdb_column_family_handle_t>?, 
    numKeys: Int, 
    keysList: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
    keysListSizes: CValuesRef<ULongVarOf<ULong>>?, 
    numValues: Int, 
    valuesList: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
    valuesListSizes: CValuesRef<ULongVarOf<ULong>>?, 
  ): Unit = 
    rocksdb_writebatch_mergev_cf(writeBatch, columnFamily, numKeys, keysList, keysListSizes, numValues, valuesList, valuesListSizes) 

  fun delete(
    key: String?, 
    keyLength: ULong, 
  ): Unit = 
    rocksdb_writebatch_delete(writeBatch, key, keyLength) 

  fun singleDelete(
    key: String?, 
    keyLength: ULong, 
  ): Unit = 
    rocksdb_writebatch_singledelete(writeBatch, key, keyLength) 

  fun delete(
    columnFamily: CValuesRef<rocksdb_column_family_handle_t>?, 
    key: String?, 
    keyLength: ULong, 
  ): Unit = 
    rocksdb_writebatch_delete_cf(writeBatch, columnFamily, key, keyLength) 

  fun deleteWithTs(
    columnFamily: CValuesRef<rocksdb_column_family_handle_t>?, 
    key: String?, 
    keyLength: ULong, 
    ts: String?, 
    tsLength: ULong, 
  ): Unit = 
    rocksdb_writebatch_delete_cf_with_ts(writeBatch, columnFamily, key, keyLength, ts, tsLength) 

  fun singleDelete(
    columnFamily: CValuesRef<rocksdb_column_family_handle_t>?, 
    key: String?, 
    keyLength: ULong, 
  ): Unit = 
    rocksdb_writebatch_singledelete_cf(writeBatch, columnFamily, key, keyLength) 

  fun singleDeleteWithTs(
    columnFamily: CValuesRef<rocksdb_column_family_handle_t>?, 
    key: String?, 
    keyLength: ULong, 
    ts: String?, 
    tsLength: ULong, 
  ): Unit = 
    rocksdb_writebatch_singledelete_cf_with_ts(writeBatch, columnFamily, key, keyLength, ts, tsLength) 

  fun delete(
    numKeys: Int, 
    keysList: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
    keysListSizes: CValuesRef<ULongVarOf<ULong>>?, 
  ): Unit = 
    rocksdb_writebatch_deletev(writeBatch, numKeys, keysList, keysListSizes) 

  fun deleteCf(
    columnFamily: CValuesRef<rocksdb_column_family_handle_t>?, 
    numKeys: Int, 
    keysList: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
    keysListSizes: CValuesRef<ULongVarOf<ULong>>?, 
  ): Unit = 
    rocksdb_writebatch_deletev_cf(writeBatch, columnFamily, numKeys, keysList, keysListSizes) 

  fun deleteRange(
    startKey: String?, 
    startKeyLen: ULong, 
    endKey: String?, 
    endKeyLen: ULong, 
  ): Unit = 
    rocksdb_writebatch_delete_range(writeBatch, startKey, startKeyLen, endKey, endKeyLen) 

  fun deleteRange(
    columnFamily: CValuesRef<rocksdb_column_family_handle_t>?, 
    startKey: String?, 
    startKeyLen: ULong, 
    endKey: String?, 
    endKeyLen: ULong, 
  ): Unit = 
    rocksdb_writebatch_delete_range_cf(writeBatch, columnFamily, startKey, startKeyLen, endKey, endKeyLen) 

  fun deleteRange(
    numKeys: Int, 
    startKeysList: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
    startKeysListSizes: CValuesRef<ULongVarOf<ULong>>?, 
    endKeysList: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
    endKeysListSizes: CValuesRef<ULongVarOf<ULong>>?, 
  ): Unit = 
    rocksdb_writebatch_delete_rangev(writeBatch, numKeys, startKeysList, startKeysListSizes, endKeysList, endKeysListSizes) 

  fun deleteRangeCf(
    columnFamily: CValuesRef<rocksdb_column_family_handle_t>?, 
    numKeys: Int, 
    startKeysList: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
    startKeysListSizes: CValuesRef<ULongVarOf<ULong>>?, 
    endKeysList: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
    endKeysListSizes: CValuesRef<ULongVarOf<ULong>>?, 
  ): Unit = 
    rocksdb_writebatch_delete_rangev_cf(writeBatch, columnFamily, numKeys, startKeysList, startKeysListSizes, endKeysList, endKeysListSizes) 

  fun putLogData(
    blob: String?, 
    len: ULong, 
  ): Unit = 
    rocksdb_writebatch_put_log_data(writeBatch, blob, len) 

  fun iterate(
    state: CValuesRef<*>?, 
    put: CPointer<CFunction<Function5<COpaquePointer?, CPointer<ByteVar>?, ULong, CPointer<ByteVar>?, ULong, Unit>>>?, 
    deleted: CPointer<CFunction<Function3<COpaquePointer?, CPointer<ByteVar>?, ULong, Unit>>>?, 
  ): Unit = 
    rocksdb_writebatch_iterate(writeBatch, state, put, deleted) 

  fun data(
    size: CValuesRef<ULongVarOf<ULong>>?, 
  ): CPointer<ByteVar>? = 
    rocksdb_writebatch_data(writeBatch, size) 

  fun setSavePoint(): Unit = 
    rocksdb_writebatch_set_save_point(writeBatch) 

  fun rollbackToSavePoint(
    errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
  ): Unit = 
    rocksdb_writebatch_rollback_to_save_point(writeBatch, errorPointer) 

  fun popSavePoint(
    errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
  ): Unit = 
    rocksdb_writebatch_pop_save_point(writeBatch, errorPointer) 

  companion object {
    fun create(): CPointer<rocksdb_writebatch_t> = 
      rocksdb_writebatch_create() 
       ?: error("could not execute 'rocksdb_writebatch_create'")
    
    fun createFrom(
      rep: String?, 
      size: ULong, 
    ): CPointer<rocksdb_writebatch_t> = 
      rocksdb_writebatch_create_from(rep, size) 
       ?: error("could not execute 'rocksdb_writebatch_create_from'")
  }
}

class WriteBatchWi(
  private val writeBatchWi: CPointer<rocksdb_writebatch_wi_t>?
) {
  fun destroy(): Unit = 
    rocksdb_writebatch_wi_destroy(writeBatchWi) 

  fun clear(): Unit = 
    rocksdb_writebatch_wi_clear(writeBatchWi) 

  fun count(): Int = 
    rocksdb_writebatch_wi_count(writeBatchWi) 

  fun put(
    key: String?, 
    keyLength: ULong, 
    value: String?, 
    valueLength: ULong, 
  ): Unit = 
    rocksdb_writebatch_wi_put(writeBatchWi, key, keyLength, value, valueLength) 

  fun put(
    columnFamily: CValuesRef<rocksdb_column_family_handle_t>?, 
    key: String?, 
    keyLength: ULong, 
    value: String?, 
    valueLength: ULong, 
  ): Unit = 
    rocksdb_writebatch_wi_put_cf(writeBatchWi, columnFamily, key, keyLength, value, valueLength) 

  fun put(
    numKeys: Int, 
    keysList: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
    keysListSizes: CValuesRef<ULongVarOf<ULong>>?, 
    numValues: Int, 
    valuesList: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
    valuesListSizes: CValuesRef<ULongVarOf<ULong>>?, 
  ): Unit = 
    rocksdb_writebatch_wi_putv(writeBatchWi, numKeys, keysList, keysListSizes, numValues, valuesList, valuesListSizes) 

  fun put(
    columnFamily: CValuesRef<rocksdb_column_family_handle_t>?, 
    numKeys: Int, 
    keysList: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
    keysListSizes: CValuesRef<ULongVarOf<ULong>>?, 
    numValues: Int, 
    valuesList: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
    valuesListSizes: CValuesRef<ULongVarOf<ULong>>?, 
  ): Unit = 
    rocksdb_writebatch_wi_putv_cf(writeBatchWi, columnFamily, numKeys, keysList, keysListSizes, numValues, valuesList, valuesListSizes) 

  fun merge(
    key: String?, 
    keyLength: ULong, 
    value: String?, 
    valueLength: ULong, 
  ): Unit = 
    rocksdb_writebatch_wi_merge(writeBatchWi, key, keyLength, value, valueLength) 

  fun merge(
    columnFamily: CValuesRef<rocksdb_column_family_handle_t>?, 
    key: String?, 
    keyLength: ULong, 
    value: String?, 
    valueLength: ULong, 
  ): Unit = 
    rocksdb_writebatch_wi_merge_cf(writeBatchWi, columnFamily, key, keyLength, value, valueLength) 

  fun merge(
    numKeys: Int, 
    keysList: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
    keysListSizes: CValuesRef<ULongVarOf<ULong>>?, 
    numValues: Int, 
    valuesList: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
    valuesListSizes: CValuesRef<ULongVarOf<ULong>>?, 
  ): Unit = 
    rocksdb_writebatch_wi_mergev(writeBatchWi, numKeys, keysList, keysListSizes, numValues, valuesList, valuesListSizes) 

  fun mergeCf(
    columnFamily: CValuesRef<rocksdb_column_family_handle_t>?, 
    numKeys: Int, 
    keysList: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
    keysListSizes: CValuesRef<ULongVarOf<ULong>>?, 
    numValues: Int, 
    valuesList: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
    valuesListSizes: CValuesRef<ULongVarOf<ULong>>?, 
  ): Unit = 
    rocksdb_writebatch_wi_mergev_cf(writeBatchWi, columnFamily, numKeys, keysList, keysListSizes, numValues, valuesList, valuesListSizes) 

  fun delete(
    key: String?, 
    keyLength: ULong, 
  ): Unit = 
    rocksdb_writebatch_wi_delete(writeBatchWi, key, keyLength) 

  fun singleDelete(
    key: String?, 
    keyLength: ULong, 
  ): Unit = 
    rocksdb_writebatch_wi_singledelete(writeBatchWi, key, keyLength) 

  fun delete(
    columnFamily: CValuesRef<rocksdb_column_family_handle_t>?, 
    key: String?, 
    keyLength: ULong, 
  ): Unit = 
    rocksdb_writebatch_wi_delete_cf(writeBatchWi, columnFamily, key, keyLength) 

  fun singleDelete(
    columnFamily: CValuesRef<rocksdb_column_family_handle_t>?, 
    key: String?, 
    keyLength: ULong, 
  ): Unit = 
    rocksdb_writebatch_wi_singledelete_cf(writeBatchWi, columnFamily, key, keyLength) 

  fun delete(
    numKeys: Int, 
    keysList: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
    keysListSizes: CValuesRef<ULongVarOf<ULong>>?, 
  ): Unit = 
    rocksdb_writebatch_wi_deletev(writeBatchWi, numKeys, keysList, keysListSizes) 

  fun deleteCf(
    columnFamily: CValuesRef<rocksdb_column_family_handle_t>?, 
    numKeys: Int, 
    keysList: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
    keysListSizes: CValuesRef<ULongVarOf<ULong>>?, 
  ): Unit = 
    rocksdb_writebatch_wi_deletev_cf(writeBatchWi, columnFamily, numKeys, keysList, keysListSizes) 

  fun deleteRange(
    startKey: String?, 
    startKeyLen: ULong, 
    endKey: String?, 
    endKeyLen: ULong, 
  ): Unit = 
    rocksdb_writebatch_wi_delete_range(writeBatchWi, startKey, startKeyLen, endKey, endKeyLen) 

  fun deleteRange(
    columnFamily: CValuesRef<rocksdb_column_family_handle_t>?, 
    startKey: String?, 
    startKeyLen: ULong, 
    endKey: String?, 
    endKeyLen: ULong, 
  ): Unit = 
    rocksdb_writebatch_wi_delete_range_cf(writeBatchWi, columnFamily, startKey, startKeyLen, endKey, endKeyLen) 

  fun deleteRange(
    numKeys: Int, 
    startKeysList: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
    startKeysListSizes: CValuesRef<ULongVarOf<ULong>>?, 
    endKeysList: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
    endKeysListSizes: CValuesRef<ULongVarOf<ULong>>?, 
  ): Unit = 
    rocksdb_writebatch_wi_delete_rangev(writeBatchWi, numKeys, startKeysList, startKeysListSizes, endKeysList, endKeysListSizes) 

  fun deleteRangeCf(
    columnFamily: CValuesRef<rocksdb_column_family_handle_t>?, 
    numKeys: Int, 
    startKeysList: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
    startKeysListSizes: CValuesRef<ULongVarOf<ULong>>?, 
    endKeysList: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
    endKeysListSizes: CValuesRef<ULongVarOf<ULong>>?, 
  ): Unit = 
    rocksdb_writebatch_wi_delete_rangev_cf(writeBatchWi, columnFamily, numKeys, startKeysList, startKeysListSizes, endKeysList, endKeysListSizes) 

  fun putLogData(
    blob: String?, 
    len: ULong, 
  ): Unit = 
    rocksdb_writebatch_wi_put_log_data(writeBatchWi, blob, len) 

  fun iterate(
    state: CValuesRef<*>?, 
    put: CPointer<CFunction<Function5<COpaquePointer?, CPointer<ByteVar>?, ULong, CPointer<ByteVar>?, ULong, Unit>>>?, 
    deleted: CPointer<CFunction<Function3<COpaquePointer?, CPointer<ByteVar>?, ULong, Unit>>>?, 
  ): Unit = 
    rocksdb_writebatch_wi_iterate(writeBatchWi, state, put, deleted) 

  fun data(
    size: CValuesRef<ULongVarOf<ULong>>?, 
  ): CPointer<ByteVar>? = 
    rocksdb_writebatch_wi_data(writeBatchWi, size) 

  fun setSavePoint(): Unit = 
    rocksdb_writebatch_wi_set_save_point(writeBatchWi) 

  fun rollbackToSavePoint(
    errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
  ): Unit = 
    rocksdb_writebatch_wi_rollback_to_save_point(writeBatchWi, errorPointer) 

  fun getFromBatch(
    options: CValuesRef<rocksdb_options_t>?, 
    key: String?, 
    keyLength: ULong, 
    valueLength: CValuesRef<ULongVarOf<ULong>>?, 
    errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
  ): CPointer<ByteVar>? = 
    rocksdb_writebatch_wi_get_from_batch(writeBatchWi, options, key, keyLength, valueLength, errorPointer) 

  fun getFromBatchCf(
    options: CValuesRef<rocksdb_options_t>?, 
    columnFamily: CValuesRef<rocksdb_column_family_handle_t>?, 
    key: String?, 
    keyLength: ULong, 
    valueLength: CValuesRef<ULongVarOf<ULong>>?, 
    errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
  ): CPointer<ByteVar>? = 
    rocksdb_writebatch_wi_get_from_batch_cf(writeBatchWi, options, columnFamily, key, keyLength, valueLength, errorPointer) 

  fun getFromBatchAndDb(
    db: CValuesRef<rocksdb_t>?, 
    options: CValuesRef<rocksdb_readoptions_t>?, 
    key: String?, 
    keyLength: ULong, 
    valueLength: CValuesRef<ULongVarOf<ULong>>?, 
    errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
  ): CPointer<ByteVar>? = 
    rocksdb_writebatch_wi_get_from_batch_and_db(writeBatchWi, db, options, key, keyLength, valueLength, errorPointer) 

  fun getFromBatchAndDbCf(
    db: CValuesRef<rocksdb_t>?, 
    options: CValuesRef<rocksdb_readoptions_t>?, 
    columnFamily: CValuesRef<rocksdb_column_family_handle_t>?, 
    key: String?, 
    keyLength: ULong, 
    valueLength: CValuesRef<ULongVarOf<ULong>>?, 
    errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
  ): CPointer<ByteVar>? = 
    rocksdb_writebatch_wi_get_from_batch_and_db_cf(writeBatchWi, db, options, columnFamily, key, keyLength, valueLength, errorPointer) 

  fun createIteratorWithBase(
    baseIterator: CValuesRef<rocksdb_iterator_t>?, 
  ): CPointer<rocksdb_iterator_t>? = 
    rocksdb_writebatch_wi_create_iterator_with_base(writeBatchWi, baseIterator) 

  fun createIteratorWithBaseCf(
    baseIterator: CValuesRef<rocksdb_iterator_t>?, 
    cf: CValuesRef<rocksdb_column_family_handle_t>?, 
  ): CPointer<rocksdb_iterator_t>? = 
    rocksdb_writebatch_wi_create_iterator_with_base_cf(writeBatchWi, baseIterator, cf) 

  companion object {
    fun create(
      reservedBytes: ULong, 
      overwriteKeys: Boolean, 
    ): CPointer<rocksdb_writebatch_wi_t> = 
      rocksdb_writebatch_wi_create(reservedBytes, overwriteKeys.toUByte()) 
       ?: error("could not execute 'rocksdb_writebatch_wi_create'")
    
    fun createFrom(
      rep: String?, 
      size: ULong, 
    ): CPointer<rocksdb_writebatch_wi_t> = 
      rocksdb_writebatch_wi_create_from(rep, size) 
       ?: error("could not execute 'rocksdb_writebatch_wi_create_from'")
  }
}

class BlockBasedOptions(
  private val blockBasedOptions: CPointer<rocksdb_block_based_table_options_t>?
) {
  fun destroy(): Unit = 
    rocksdb_block_based_options_destroy(blockBasedOptions) 

  fun setChecksum(
    arg1: Byte, 
  ): Unit = 
    rocksdb_block_based_options_set_checksum(blockBasedOptions, arg1) 

  fun setBlockSize(
    blockSize: ULong, 
  ): Unit = 
    rocksdb_block_based_options_set_block_size(blockBasedOptions, blockSize) 

  fun setBlockSizeDeviation(
    blockSizeDeviation: Int, 
  ): Unit = 
    rocksdb_block_based_options_set_block_size_deviation(blockBasedOptions, blockSizeDeviation) 

  fun setBlockRestartInterval(
    blockRestartInterval: Int, 
  ): Unit = 
    rocksdb_block_based_options_set_block_restart_interval(blockBasedOptions, blockRestartInterval) 

  fun setIndexBlockRestartInterval(
    indexBlockRestartInterval: Int, 
  ): Unit = 
    rocksdb_block_based_options_set_index_block_restart_interval(blockBasedOptions, indexBlockRestartInterval) 

  fun setMetadataBlockSize(
    metadataBlockSize: ULong, 
  ): Unit = 
    rocksdb_block_based_options_set_metadata_block_size(blockBasedOptions, metadataBlockSize) 

  fun setPartitionFilters(
    partitionFilters: Boolean, 
  ): Unit = 
    rocksdb_block_based_options_set_partition_filters(blockBasedOptions, partitionFilters.toUByte()) 

  fun setUseDeltaEncoding(
    useDeltaEncoding: Boolean, 
  ): Unit = 
    rocksdb_block_based_options_set_use_delta_encoding(blockBasedOptions, useDeltaEncoding.toUByte()) 

  fun setFilterPolicy(
    filterPolicy: CValuesRef<rocksdb_filterpolicy_t>?, 
  ): Unit = 
    rocksdb_block_based_options_set_filter_policy(blockBasedOptions, filterPolicy) 

  fun setNoBlockCache(
    noBlockCache: Boolean, 
  ): Unit = 
    rocksdb_block_based_options_set_no_block_cache(blockBasedOptions, noBlockCache.toUByte()) 

  fun setBlockCache(
    blockCache: CValuesRef<rocksdb_cache_t>?, 
  ): Unit = 
    rocksdb_block_based_options_set_block_cache(blockBasedOptions, blockCache) 

  fun setBlockCacheCompressed(
    blockCacheCompressed: CValuesRef<rocksdb_cache_t>?, 
  ): Unit = 
    rocksdb_block_based_options_set_block_cache_compressed(blockBasedOptions, blockCacheCompressed) 

  fun setWholeKeyFiltering(
    arg1: Boolean, 
  ): Unit = 
    rocksdb_block_based_options_set_whole_key_filtering(blockBasedOptions, arg1.toUByte()) 

  fun setFormatVersion(
    arg1: Int, 
  ): Unit = 
    rocksdb_block_based_options_set_format_version(blockBasedOptions, arg1) 

  fun setIndexType(
    arg1: Int, 
  ): Unit = 
    rocksdb_block_based_options_set_index_type(blockBasedOptions, arg1) 

  fun setDataBlockIndexType(
    arg1: Int, 
  ): Unit = 
    rocksdb_block_based_options_set_data_block_index_type(blockBasedOptions, arg1) 

  fun setDataBlockHashRatio(
    v: Double, 
  ): Unit = 
    rocksdb_block_based_options_set_data_block_hash_ratio(blockBasedOptions, v) 

  fun setCacheIndexAndFilterBlocks(
    arg1: Boolean, 
  ): Unit = 
    rocksdb_block_based_options_set_cache_index_and_filter_blocks(blockBasedOptions, arg1.toUByte()) 

  fun setCacheIndexAndFilterBlocksWithHighPriority(
    arg1: Boolean, 
  ): Unit = 
    rocksdb_block_based_options_set_cache_index_and_filter_blocks_with_high_priority(blockBasedOptions, arg1.toUByte()) 

  fun setPinL0FilterAndIndexBlocksInCache(
    arg1: Boolean, 
  ): Unit = 
    rocksdb_block_based_options_set_pin_l0_filter_and_index_blocks_in_cache(blockBasedOptions, arg1.toUByte()) 

  fun setPinTopLevelIndexAndFilter(
    arg1: Boolean, 
  ): Unit = 
    rocksdb_block_based_options_set_pin_top_level_index_and_filter(blockBasedOptions, arg1.toUByte()) 

  companion object {
    fun create(): CPointer<rocksdb_block_based_table_options_t> = 
      rocksdb_block_based_options_create() 
       ?: error("could not execute 'rocksdb_block_based_options_create'")
  }
}

class CuckooOptions(
  private val cuckooOptions: CPointer<rocksdb_cuckoo_table_options_t>?
) {
  fun destroy(): Unit = 
    rocksdb_cuckoo_options_destroy(cuckooOptions) 

  fun setHashRatio(
    v: Double, 
  ): Unit = 
    rocksdb_cuckoo_options_set_hash_ratio(cuckooOptions, v) 

  fun setMaxSearchDepth(
    v: UInt, 
  ): Unit = 
    rocksdb_cuckoo_options_set_max_search_depth(cuckooOptions, v) 

  fun setCuckooBlockSize(
    v: UInt, 
  ): Unit = 
    rocksdb_cuckoo_options_set_cuckoo_block_size(cuckooOptions, v) 

  fun setIdentityAsFirstHash(
    v: Boolean, 
  ): Unit = 
    rocksdb_cuckoo_options_set_identity_as_first_hash(cuckooOptions, v.toUByte()) 

  fun setUseModuleHash(
    v: Boolean, 
  ): Unit = 
    rocksdb_cuckoo_options_set_use_module_hash(cuckooOptions, v.toUByte()) 

  companion object {
    fun create(): CPointer<rocksdb_cuckoo_table_options_t> = 
      rocksdb_cuckoo_options_create() 
       ?: error("could not execute 'rocksdb_cuckoo_options_create'")
  }
}

class RocksDbOptions(
  private val rocksDbOptions: CPointer<rocksdb_options_t>?
) {
  fun setBlockBasedTableFactory(
    tableOptions: CValuesRef<rocksdb_block_based_table_options_t>?, 
  ): Unit = 
    rocksdb_options_set_block_based_table_factory(rocksDbOptions, tableOptions) 

  fun setCuckooTableFactory(
    tableOptions: CValuesRef<rocksdb_cuckoo_table_options_t>?, 
  ): Unit = 
    rocksdb_options_set_cuckoo_table_factory(rocksDbOptions, tableOptions) 

  fun destroy(): Unit = 
    rocksdb_options_destroy(rocksDbOptions) 

  fun createCopy(): CPointer<rocksdb_options_t>? = 
    rocksdb_options_create_copy(rocksDbOptions) 

  fun increaseParallelism(
    totalThreads: Int, 
  ): Unit = 
    rocksdb_options_increase_parallelism(rocksDbOptions, totalThreads) 

  fun optimizeForPointLookup(
    blockCacheSizemb: ULong, 
  ): Unit = 
    rocksdb_options_optimize_for_point_lookup(rocksDbOptions, blockCacheSizemb) 

  fun optimizeLevelStyleCompaction(
    memtableMemoryBudget: ULong, 
  ): Unit = 
    rocksdb_options_optimize_level_style_compaction(rocksDbOptions, memtableMemoryBudget) 

  fun optimizeUniversalStyleCompaction(
    memtableMemoryBudget: ULong, 
  ): Unit = 
    rocksdb_options_optimize_universal_style_compaction(rocksDbOptions, memtableMemoryBudget) 

  fun setCompactionFilter(
    arg1: CValuesRef<rocksdb_compactionfilter_t>?, 
  ): Unit = 
    rocksdb_options_set_compaction_filter(rocksDbOptions, arg1) 

  fun setCompactionFilterFactory(
    arg1: CValuesRef<rocksdb_compactionfilterfactory_t>?, 
  ): Unit = 
    rocksdb_options_set_compaction_filter_factory(rocksDbOptions, arg1) 

  fun compactionReadAheadSize(
    arg1: ULong, 
  ): Unit = 
    rocksdb_options_compaction_readahead_size(rocksDbOptions, arg1) 

  fun setComparator(
    arg1: CValuesRef<rocksdb_comparator_t>?, 
  ): Unit = 
    rocksdb_options_set_comparator(rocksDbOptions, arg1) 

  fun setMergeOperator(
    arg1: CValuesRef<rocksdb_mergeoperator_t>?, 
  ): Unit = 
    rocksdb_options_set_merge_operator(rocksDbOptions, arg1) 

  fun setUint64addMergeOperator(): Unit = 
    rocksdb_options_set_uint64add_merge_operator(rocksDbOptions) 

  fun setCompressionPerLevel(
    levelValues: CValuesRef<IntVarOf<Int>>?, 
    numLevels: ULong, 
  ): Unit = 
    rocksdb_options_set_compression_per_level(rocksDbOptions, levelValues, numLevels) 

  fun setDbPaths(
    pathValues: CValuesRef<CPointerVarOf<CPointer<rocksdb_dbpath_t>>>?, 
    numPaths: ULong, 
  ): Unit = 
    rocksdb_options_set_db_paths(rocksDbOptions, pathValues, numPaths) 

  fun setEnv(
    arg1: CValuesRef<rocksdb_env_t>?, 
  ): Unit = 
    rocksdb_options_set_env(rocksDbOptions, arg1) 

  fun setInfoLog(
    arg1: CValuesRef<rocksdb_logger_t>?, 
  ): Unit = 
    rocksdb_options_set_info_log(rocksDbOptions, arg1) 

  fun setCompressionOptions(
    arg1: Int, 
    arg2: Int, 
    arg3: Int, 
    arg4: Int, 
  ): Unit = 
    rocksdb_options_set_compression_options(rocksDbOptions, arg1, arg2, arg3, arg4) 

  fun setBottommostCompressionOptions(
    arg1: Int, 
    arg2: Int, 
    arg3: Int, 
    arg4: Int, 
    arg5: Boolean, 
  ): Unit = 
    rocksdb_options_set_bottommost_compression_options(rocksDbOptions, arg1, arg2, arg3, arg4, arg5.toUByte()) 

  fun setBottommostCompressionOptionsZstdMaxTrainBytes(
    arg1: Int, 
    arg2: Boolean, 
  ): Unit = 
    rocksdb_options_set_bottommost_compression_options_zstd_max_train_bytes(rocksDbOptions, arg1, arg2.toUByte()) 

  fun setBottommostCompressionOptionsUseZstdDictTrainer(
    arg1: Boolean, 
    arg2: Boolean, 
  ): Unit = 
    rocksdb_options_set_bottommost_compression_options_use_zstd_dict_trainer(rocksDbOptions, arg1.toUByte(), arg2.toUByte()) 

  fun setBottommostCompressionOptionsMaxDictBufferBytes(
    arg1: ULong, 
    arg2: Boolean, 
  ): Unit = 
    rocksdb_options_set_bottommost_compression_options_max_dict_buffer_bytes(rocksDbOptions, arg1, arg2.toUByte()) 

  fun setPrefixExtractor(
    arg1: CValuesRef<rocksdb_slicetransform_t>?, 
  ): Unit = 
    rocksdb_options_set_prefix_extractor(rocksDbOptions, arg1) 

  fun setMaxBytesForLevelMultiplierAdditional(
    levelValues: CValuesRef<IntVarOf<Int>>?, 
    numLevels: ULong, 
  ): Unit = 
    rocksdb_options_set_max_bytes_for_level_multiplier_additional(rocksDbOptions, levelValues, numLevels) 

  fun enableStatistics(): Unit = 
    rocksdb_options_enable_statistics(rocksDbOptions) 

  fun setBlobCache(
    blobCache: CValuesRef<rocksdb_cache_t>?, 
  ): Unit = 
    rocksdb_options_set_blob_cache(rocksDbOptions, blobCache) 

  fun statisticsGetString(): CPointer<ByteVar>? = 
    rocksdb_options_statistics_get_string(rocksDbOptions) 

  fun setDbLogDir(
    arg1: String?, 
  ): Unit = 
    rocksdb_options_set_db_log_dir(rocksDbOptions, arg1) 

  fun setWalDir(
    arg1: String?, 
  ): Unit = 
    rocksdb_options_set_wal_dir(rocksDbOptions, arg1) 

  fun prepareForBulkLoad(): Unit = 
    rocksdb_options_prepare_for_bulk_load(rocksDbOptions) 

  fun setMemtableVectorRep(): Unit = 
    rocksdb_options_set_memtable_vector_rep(rocksDbOptions) 

  fun setHashSkipListRep(
    arg1: ULong, 
    arg2: Int, 
    arg3: Int, 
  ): Unit = 
    rocksdb_options_set_hash_skip_list_rep(rocksDbOptions, arg1, arg2, arg3) 

  fun setHashLinkListRep(
    arg1: ULong, 
  ): Unit = 
    rocksdb_options_set_hash_link_list_rep(rocksDbOptions, arg1) 

  fun setPlainTableFactory(
    arg1: UInt, 
    arg2: Int, 
    arg3: Double, 
    arg4: ULong, 
  ): Unit = 
    rocksdb_options_set_plain_table_factory(rocksDbOptions, arg1, arg2, arg3, arg4) 

  fun setMinLevelToCompress(
    level: Int, 
  ): Unit = 
    rocksdb_options_set_min_level_to_compress(rocksDbOptions, level) 

  fun setUniversalCompactionOptions(
    arg1: CValuesRef<rocksdb_universal_compaction_options_t>?, 
  ): Unit = 
    rocksdb_options_set_universal_compaction_options(rocksDbOptions, arg1) 

  fun setFifoCompactionOptions(
    fifo: CValuesRef<rocksdb_fifo_compaction_options_t>?, 
  ): Unit = 
    rocksdb_options_set_fifo_compaction_options(rocksDbOptions, fifo) 

  fun setRatelimiter(
    limiter: CValuesRef<rocksdb_ratelimiter_t>?, 
  ): Unit = 
    rocksdb_options_set_ratelimiter(rocksDbOptions, limiter) 

  fun setRowCache(
    cache: CValuesRef<rocksdb_cache_t>?, 
  ): Unit = 
    rocksdb_options_set_row_cache(rocksDbOptions, cache) 

  fun addCompactOnDeletionCollectorFactory(
    windowSize: ULong, 
    numDelsTrigger: ULong, 
  ): Unit = 
    rocksdb_options_add_compact_on_deletion_collector_factory(rocksDbOptions, windowSize, numDelsTrigger) 

  fun setDumpMallocStats(
    arg1: Boolean, 
  ): Unit = 
    rocksdb_options_set_dump_malloc_stats(rocksDbOptions, arg1.toUByte()) 

  fun setMemtableWholeKeyFiltering(
    arg1: Boolean, 
  ): Unit = 
    rocksdb_options_set_memtable_whole_key_filtering(rocksDbOptions, arg1.toUByte()) 

  var allowIngestBehind: Boolean
    get() = rocksdb_options_get_allow_ingest_behind(rocksDbOptions).toBoolean()
    set(value) = rocksdb_options_set_allow_ingest_behind(rocksDbOptions, value.toUByte())

  val compactionReadAheadSize: ULong
    get() = rocksdb_options_get_compaction_readahead_size(rocksDbOptions)

  var createIfMissing: Boolean
    get() = rocksdb_options_get_create_if_missing(rocksDbOptions).toBoolean()
    set(value) = rocksdb_options_set_create_if_missing(rocksDbOptions, value.toUByte())

  var createMissingColumnFamilies: Boolean
    get() = rocksdb_options_get_create_missing_column_families(rocksDbOptions).toBoolean()
    set(value) = rocksdb_options_set_create_missing_column_families(rocksDbOptions, value.toUByte())

  var errorIfExists: Boolean
    get() = rocksdb_options_get_error_if_exists(rocksDbOptions).toBoolean()
    set(value) = rocksdb_options_set_error_if_exists(rocksDbOptions, value.toUByte())

  var paranoidChecks: Boolean
    get() = rocksdb_options_get_paranoid_checks(rocksDbOptions).toBoolean()
    set(value) = rocksdb_options_set_paranoid_checks(rocksDbOptions, value.toUByte())

  var infoLogLevel: Int
    get() = rocksdb_options_get_info_log_level(rocksDbOptions)
    set(value) = rocksdb_options_set_info_log_level(rocksDbOptions, value)

  var writeBufferSize: ULong
    get() = rocksdb_options_get_write_buffer_size(rocksDbOptions)
    set(value) = rocksdb_options_set_write_buffer_size(rocksDbOptions, value)

  var dbWriteBufferSize: ULong
    get() = rocksdb_options_get_db_write_buffer_size(rocksDbOptions)
    set(value) = rocksdb_options_set_db_write_buffer_size(rocksDbOptions, value)

  var maxOpenFiles: Int
    get() = rocksdb_options_get_max_open_files(rocksDbOptions)
    set(value) = rocksdb_options_set_max_open_files(rocksDbOptions, value)

  var maxFileOpeningThreads: Int
    get() = rocksdb_options_get_max_file_opening_threads(rocksDbOptions)
    set(value) = rocksdb_options_set_max_file_opening_threads(rocksDbOptions, value)

  var maxTotalWalSize: ULong
    get() = rocksdb_options_get_max_total_wal_size(rocksDbOptions)
    set(value) = rocksdb_options_set_max_total_wal_size(rocksDbOptions, value)

  var compressionOptionsZstdMaxTrainBytes: Int
    get() = rocksdb_options_get_compression_options_zstd_max_train_bytes(rocksDbOptions)
    set(value) = rocksdb_options_set_compression_options_zstd_max_train_bytes(rocksDbOptions, value)

  var compressionOptionsUseZstdDictTrainer: Boolean
    get() = rocksdb_options_get_compression_options_use_zstd_dict_trainer(rocksDbOptions).toBoolean()
    set(value) = rocksdb_options_set_compression_options_use_zstd_dict_trainer(rocksDbOptions, value.toUByte())

  var compressionOptionsParallelThreads: Int
    get() = rocksdb_options_get_compression_options_parallel_threads(rocksDbOptions)
    set(value) = rocksdb_options_set_compression_options_parallel_threads(rocksDbOptions, value)

  var compressionOptionsMaxDictBufferBytes: ULong
    get() = rocksdb_options_get_compression_options_max_dict_buffer_bytes(rocksDbOptions)
    set(value) = rocksdb_options_set_compression_options_max_dict_buffer_bytes(rocksDbOptions, value)

  val bottommostCompressionOptionsUseZstdDictTrainer: Boolean
    get() = rocksdb_options_get_bottommost_compression_options_use_zstd_dict_trainer(rocksDbOptions).toBoolean()

  var numLevels: Int
    get() = rocksdb_options_get_num_levels(rocksDbOptions)
    set(value) = rocksdb_options_set_num_levels(rocksDbOptions, value)

  var level0FileNumCompactionTrigger: Int
    get() = rocksdb_options_get_level0_file_num_compaction_trigger(rocksDbOptions)
    set(value) = rocksdb_options_set_level0_file_num_compaction_trigger(rocksDbOptions, value)

  var level0SlowdownWritesTrigger: Int
    get() = rocksdb_options_get_level0_slowdown_writes_trigger(rocksDbOptions)
    set(value) = rocksdb_options_set_level0_slowdown_writes_trigger(rocksDbOptions, value)

  var level0StopWritesTrigger: Int
    get() = rocksdb_options_get_level0_stop_writes_trigger(rocksDbOptions)
    set(value) = rocksdb_options_set_level0_stop_writes_trigger(rocksDbOptions, value)

  var targetFileSizeBase: ULong
    get() = rocksdb_options_get_target_file_size_base(rocksDbOptions)
    set(value) = rocksdb_options_set_target_file_size_base(rocksDbOptions, value)

  var targetFileSizeMultiplier: Int
    get() = rocksdb_options_get_target_file_size_multiplier(rocksDbOptions)
    set(value) = rocksdb_options_set_target_file_size_multiplier(rocksDbOptions, value)

  var maxBytesForLevelBase: ULong
    get() = rocksdb_options_get_max_bytes_for_level_base(rocksDbOptions)
    set(value) = rocksdb_options_set_max_bytes_for_level_base(rocksDbOptions, value)

  var levelCompactionDynamicLevelBytes: Boolean
    get() = rocksdb_options_get_level_compaction_dynamic_level_bytes(rocksDbOptions).toBoolean()
    set(value) = rocksdb_options_set_level_compaction_dynamic_level_bytes(rocksDbOptions, value.toUByte())

  var maxBytesForLevelMultiplier: Double
    get() = rocksdb_options_get_max_bytes_for_level_multiplier(rocksDbOptions)
    set(value) = rocksdb_options_set_max_bytes_for_level_multiplier(rocksDbOptions, value)

  var skipStatsUpdateOnDbOpen: Boolean
    get() = rocksdb_options_get_skip_stats_update_on_db_open(rocksDbOptions).toBoolean()
    set(value) = rocksdb_options_set_skip_stats_update_on_db_open(rocksDbOptions, value.toUByte())

  var skipCheckingSstFileSizesOnDbOpen: Boolean
    get() = rocksdb_options_get_skip_checking_sst_file_sizes_on_db_open(rocksDbOptions).toBoolean()
    set(value) = rocksdb_options_set_skip_checking_sst_file_sizes_on_db_open(rocksDbOptions, value.toUByte())

  var enableBlobFiles: Boolean
    get() = rocksdb_options_get_enable_blob_files(rocksDbOptions).toBoolean()
    set(value) = rocksdb_options_set_enable_blob_files(rocksDbOptions, value.toUByte())

  var minBlobSize: ULong
    get() = rocksdb_options_get_min_blob_size(rocksDbOptions)
    set(value) = rocksdb_options_set_min_blob_size(rocksDbOptions, value)

  var blobFileSize: ULong
    get() = rocksdb_options_get_blob_file_size(rocksDbOptions)
    set(value) = rocksdb_options_set_blob_file_size(rocksDbOptions, value)

  var blobCompressionType: Int
    get() = rocksdb_options_get_blob_compression_type(rocksDbOptions)
    set(value) = rocksdb_options_set_blob_compression_type(rocksDbOptions, value)

  var enableBlobGc: Boolean
    get() = rocksdb_options_get_enable_blob_gc(rocksDbOptions).toBoolean()
    set(value) = rocksdb_options_set_enable_blob_gc(rocksDbOptions, value.toUByte())

  var blobGcAgeCutoff: Double
    get() = rocksdb_options_get_blob_gc_age_cutoff(rocksDbOptions)
    set(value) = rocksdb_options_set_blob_gc_age_cutoff(rocksDbOptions, value)

  var blobGcForceThreshold: Double
    get() = rocksdb_options_get_blob_gc_force_threshold(rocksDbOptions)
    set(value) = rocksdb_options_set_blob_gc_force_threshold(rocksDbOptions, value)

  var blobCompactionReadAheadSize: ULong
    get() = rocksdb_options_get_blob_compaction_readahead_size(rocksDbOptions)
    set(value) = rocksdb_options_set_blob_compaction_readahead_size(rocksDbOptions, value)

  var blobFileStartingLevel: Int
    get() = rocksdb_options_get_blob_file_starting_level(rocksDbOptions)
    set(value) = rocksdb_options_set_blob_file_starting_level(rocksDbOptions, value)

  var prepopulateBlobCache: Int
    get() = rocksdb_options_get_prepopulate_blob_cache(rocksDbOptions)
    set(value) = rocksdb_options_set_prepopulate_blob_cache(rocksDbOptions, value)

  var maxWriteBufferNumber: Int
    get() = rocksdb_options_get_max_write_buffer_number(rocksDbOptions)
    set(value) = rocksdb_options_set_max_write_buffer_number(rocksDbOptions, value)

  var minWriteBufferNumberToMerge: Int
    get() = rocksdb_options_get_min_write_buffer_number_to_merge(rocksDbOptions)
    set(value) = rocksdb_options_set_min_write_buffer_number_to_merge(rocksDbOptions, value)

  var maxWriteBufferNumberToMaintain: Int
    get() = rocksdb_options_get_max_write_buffer_number_to_maintain(rocksDbOptions)
    set(value) = rocksdb_options_set_max_write_buffer_number_to_maintain(rocksDbOptions, value)

  var maxWriteBufferSizeToMaintain: Long
    get() = rocksdb_options_get_max_write_buffer_size_to_maintain(rocksDbOptions)
    set(value) = rocksdb_options_set_max_write_buffer_size_to_maintain(rocksDbOptions, value)

  var enablePipelinedWrite: Boolean
    get() = rocksdb_options_get_enable_pipelined_write(rocksDbOptions).toBoolean()
    set(value) = rocksdb_options_set_enable_pipelined_write(rocksDbOptions, value.toUByte())

  var unorderedWrite: Boolean
    get() = rocksdb_options_get_unordered_write(rocksDbOptions).toBoolean()
    set(value) = rocksdb_options_set_unordered_write(rocksDbOptions, value.toUByte())

  var maxSubCompactions: UInt
    get() = rocksdb_options_get_max_subcompactions(rocksDbOptions)
    set(value) = rocksdb_options_set_max_subcompactions(rocksDbOptions, value)

  var maxBackgroundJobs: Int
    get() = rocksdb_options_get_max_background_jobs(rocksDbOptions)
    set(value) = rocksdb_options_set_max_background_jobs(rocksDbOptions, value)

  var maxBackgroundCompactions: Int
    get() = rocksdb_options_get_max_background_compactions(rocksDbOptions)
    set(value) = rocksdb_options_set_max_background_compactions(rocksDbOptions, value)

  var maxBackgroundFlushes: Int
    get() = rocksdb_options_get_max_background_flushes(rocksDbOptions)
    set(value) = rocksdb_options_set_max_background_flushes(rocksDbOptions, value)

  var maxLogFileSize: ULong
    get() = rocksdb_options_get_max_log_file_size(rocksDbOptions)
    set(value) = rocksdb_options_set_max_log_file_size(rocksDbOptions, value)

  var logFileTimeToRoll: ULong
    get() = rocksdb_options_get_log_file_time_to_roll(rocksDbOptions)
    set(value) = rocksdb_options_set_log_file_time_to_roll(rocksDbOptions, value)

  var keepLogFileNum: ULong
    get() = rocksdb_options_get_keep_log_file_num(rocksDbOptions)
    set(value) = rocksdb_options_set_keep_log_file_num(rocksDbOptions, value)

  var recycleLogFileNum: ULong
    get() = rocksdb_options_get_recycle_log_file_num(rocksDbOptions)
    set(value) = rocksdb_options_set_recycle_log_file_num(rocksDbOptions, value)

  var softPendingCompactionBytesLimit: ULong
    get() = rocksdb_options_get_soft_pending_compaction_bytes_limit(rocksDbOptions)
    set(value) = rocksdb_options_set_soft_pending_compaction_bytes_limit(rocksDbOptions, value)

  var hardPendingCompactionBytesLimit: ULong
    get() = rocksdb_options_get_hard_pending_compaction_bytes_limit(rocksDbOptions)
    set(value) = rocksdb_options_set_hard_pending_compaction_bytes_limit(rocksDbOptions, value)

  var maxManifestFileSize: ULong
    get() = rocksdb_options_get_max_manifest_file_size(rocksDbOptions)
    set(value) = rocksdb_options_set_max_manifest_file_size(rocksDbOptions, value)

  var tableCacheNumshardbits: Int
    get() = rocksdb_options_get_table_cache_numshardbits(rocksDbOptions)
    set(value) = rocksdb_options_set_table_cache_numshardbits(rocksDbOptions, value)

  var arenaBlockSize: ULong
    get() = rocksdb_options_get_arena_block_size(rocksDbOptions)
    set(value) = rocksdb_options_set_arena_block_size(rocksDbOptions, value)

  var useFsync: Int
    get() = rocksdb_options_get_use_fsync(rocksDbOptions)
    set(value) = rocksdb_options_set_use_fsync(rocksDbOptions, value)

  var walTtlSeconds: ULong
    get() = rocksdb_options_get_WAL_ttl_seconds(rocksDbOptions)
    set(value) = rocksdb_options_set_WAL_ttl_seconds(rocksDbOptions, value)

  var walSizeLimitMB: ULong
    get() = rocksdb_options_get_WAL_size_limit_MB(rocksDbOptions)
    set(value) = rocksdb_options_set_WAL_size_limit_MB(rocksDbOptions, value)

  var manifestPreallocationSize: ULong
    get() = rocksdb_options_get_manifest_preallocation_size(rocksDbOptions)
    set(value) = rocksdb_options_set_manifest_preallocation_size(rocksDbOptions, value)

  var allowMmapReads: Boolean
    get() = rocksdb_options_get_allow_mmap_reads(rocksDbOptions).toBoolean()
    set(value) = rocksdb_options_set_allow_mmap_reads(rocksDbOptions, value.toUByte())

  var allowMmapWrites: Boolean
    get() = rocksdb_options_get_allow_mmap_writes(rocksDbOptions).toBoolean()
    set(value) = rocksdb_options_set_allow_mmap_writes(rocksDbOptions, value.toUByte())

  var useDirectReads: Boolean
    get() = rocksdb_options_get_use_direct_reads(rocksDbOptions).toBoolean()
    set(value) = rocksdb_options_set_use_direct_reads(rocksDbOptions, value.toUByte())

  var useDirectIoForFlushAndCompaction: Boolean
    get() = rocksdb_options_get_use_direct_io_for_flush_and_compaction(rocksDbOptions).toBoolean()
    set(value) = rocksdb_options_set_use_direct_io_for_flush_and_compaction(rocksDbOptions, value.toUByte())

  var isFdCloseOnExec: Boolean
    get() = rocksdb_options_get_is_fd_close_on_exec(rocksDbOptions).toBoolean()
    set(value) = rocksdb_options_set_is_fd_close_on_exec(rocksDbOptions, value.toUByte())

  var statsDumpPeriodSec: UInt
    get() = rocksdb_options_get_stats_dump_period_sec(rocksDbOptions)
    set(value) = rocksdb_options_set_stats_dump_period_sec(rocksDbOptions, value)

  var statsPersistPeriodSec: UInt
    get() = rocksdb_options_get_stats_persist_period_sec(rocksDbOptions)
    set(value) = rocksdb_options_set_stats_persist_period_sec(rocksDbOptions, value)

  var adviseRandomOnOpen: Boolean
    get() = rocksdb_options_get_advise_random_on_open(rocksDbOptions).toBoolean()
    set(value) = rocksdb_options_set_advise_random_on_open(rocksDbOptions, value.toUByte())

  var accessHintOnCompactionStart: Int
    get() = rocksdb_options_get_access_hint_on_compaction_start(rocksDbOptions)
    set(value) = rocksdb_options_set_access_hint_on_compaction_start(rocksDbOptions, value)

  var useAdaptiveMutex: Boolean
    get() = rocksdb_options_get_use_adaptive_mutex(rocksDbOptions).toBoolean()
    set(value) = rocksdb_options_set_use_adaptive_mutex(rocksDbOptions, value.toUByte())

  var bytesPerSync: ULong
    get() = rocksdb_options_get_bytes_per_sync(rocksDbOptions)
    set(value) = rocksdb_options_set_bytes_per_sync(rocksDbOptions, value)

  var walBytesPerSync: ULong
    get() = rocksdb_options_get_wal_bytes_per_sync(rocksDbOptions)
    set(value) = rocksdb_options_set_wal_bytes_per_sync(rocksDbOptions, value)

  var writableFileMaxBufferSize: ULong
    get() = rocksdb_options_get_writable_file_max_buffer_size(rocksDbOptions)
    set(value) = rocksdb_options_set_writable_file_max_buffer_size(rocksDbOptions, value)

  var allowConcurrentMemtableWrite: Boolean
    get() = rocksdb_options_get_allow_concurrent_memtable_write(rocksDbOptions).toBoolean()
    set(value) = rocksdb_options_set_allow_concurrent_memtable_write(rocksDbOptions, value.toUByte())

  var enableWriteThreadAdaptiveYield: Boolean
    get() = rocksdb_options_get_enable_write_thread_adaptive_yield(rocksDbOptions).toBoolean()
    set(value) = rocksdb_options_set_enable_write_thread_adaptive_yield(rocksDbOptions, value.toUByte())

  var maxSequentialSkipInIterations: ULong
    get() = rocksdb_options_get_max_sequential_skip_in_iterations(rocksDbOptions)
    set(value) = rocksdb_options_set_max_sequential_skip_in_iterations(rocksDbOptions, value)

  var disableAutoCompactions: Boolean
    get() = rocksdb_options_get_disable_auto_compactions(rocksDbOptions).toBoolean()
    set(value) = rocksdb_options_set_disable_auto_compactions(rocksDbOptions, value.toInt())

  var optimizeFiltersForHits: Boolean
    get() = rocksdb_options_get_optimize_filters_for_hits(rocksDbOptions).toBoolean()
    set(value) = rocksdb_options_set_optimize_filters_for_hits(rocksDbOptions, value.toInt())

  var deleteObsoleteFilesPeriodMicros: ULong
    get() = rocksdb_options_get_delete_obsolete_files_period_micros(rocksDbOptions)
    set(value) = rocksdb_options_set_delete_obsolete_files_period_micros(rocksDbOptions, value)

  var memtablePrefixBloomSizeRatio: Double
    get() = rocksdb_options_get_memtable_prefix_bloom_size_ratio(rocksDbOptions)
    set(value) = rocksdb_options_set_memtable_prefix_bloom_size_ratio(rocksDbOptions, value)

  var maxCompactionBytes: ULong
    get() = rocksdb_options_get_max_compaction_bytes(rocksDbOptions)
    set(value) = rocksdb_options_set_max_compaction_bytes(rocksDbOptions, value)

  var memtableHugePageSize: ULong
    get() = rocksdb_options_get_memtable_huge_page_size(rocksDbOptions)
    set(value) = rocksdb_options_set_memtable_huge_page_size(rocksDbOptions, value)

  var maxSuccessiveMerges: ULong
    get() = rocksdb_options_get_max_successive_merges(rocksDbOptions)
    set(value) = rocksdb_options_set_max_successive_merges(rocksDbOptions, value)

  var bloomLocality: UInt
    get() = rocksdb_options_get_bloom_locality(rocksDbOptions)
    set(value) = rocksdb_options_set_bloom_locality(rocksDbOptions, value)

  var inplaceUpdateSupport: Boolean
    get() = rocksdb_options_get_inplace_update_support(rocksDbOptions).toBoolean()
    set(value) = rocksdb_options_set_inplace_update_support(rocksDbOptions, value.toUByte())

  var inplaceUpdateNumLocks: ULong
    get() = rocksdb_options_get_inplace_update_num_locks(rocksDbOptions)
    set(value) = rocksdb_options_set_inplace_update_num_locks(rocksDbOptions, value)

  var reportBgIoStats: Boolean
    get() = rocksdb_options_get_report_bg_io_stats(rocksDbOptions).toBoolean()
    set(value) = rocksdb_options_set_report_bg_io_stats(rocksDbOptions, value.toInt())

  var avoidUnnecessaryBlockingIo: Boolean
    get() = rocksdb_options_get_avoid_unnecessary_blocking_io(rocksDbOptions).toBoolean()
    set(value) = rocksdb_options_set_avoid_unnecessary_blocking_io(rocksDbOptions, value.toUByte())

  var experimentalMemPurgeThreshold: Double
    get() = rocksdb_options_get_experimental_mempurge_threshold(rocksDbOptions)
    set(value) = rocksdb_options_set_experimental_mempurge_threshold(rocksDbOptions, value)

  var walRecoveryMode: Int
    get() = rocksdb_options_get_wal_recovery_mode(rocksDbOptions)
    set(value) = rocksdb_options_set_wal_recovery_mode(rocksDbOptions, value)

  var compression: Int
    get() = rocksdb_options_get_compression(rocksDbOptions)
    set(value) = rocksdb_options_set_compression(rocksDbOptions, value)

  var bottommostCompression: Int
    get() = rocksdb_options_get_bottommost_compression(rocksDbOptions)
    set(value) = rocksdb_options_set_bottommost_compression(rocksDbOptions, value)

  var compactionStyle: Int
    get() = rocksdb_options_get_compaction_style(rocksDbOptions)
    set(value) = rocksdb_options_set_compaction_style(rocksDbOptions, value)

  var atomicFlush: Boolean
    get() = rocksdb_options_get_atomic_flush(rocksDbOptions).toBoolean()
    set(value) = rocksdb_options_set_atomic_flush(rocksDbOptions, value.toUByte())

  var manualWalFlush: Boolean
    get() = rocksdb_options_get_manual_wal_flush(rocksDbOptions).toBoolean()
    set(value) = rocksdb_options_set_manual_wal_flush(rocksDbOptions, value.toUByte())

  var walCompression: Int
    get() = rocksdb_options_get_wal_compression(rocksDbOptions)
    set(value) = rocksdb_options_set_wal_compression(rocksDbOptions, value)

  companion object {
    fun create(): CPointer<rocksdb_options_t> = 
      rocksdb_options_create() 
       ?: error("could not execute 'rocksdb_options_create'")
  }
}

class RateLimiter(
  private val rateLimiter: CPointer<rocksdb_ratelimiter_t>?
) {
  fun destroy(): Unit = 
    rocksdb_ratelimiter_destroy(rateLimiter) 

  companion object {
    fun create(
      rateBytesPerSec: Long, 
      refillPeriodUs: Long, 
      fairness: Int, 
    ): CPointer<rocksdb_ratelimiter_t> = 
      rocksdb_ratelimiter_create(rateBytesPerSec, refillPeriodUs, fairness) 
       ?: error("could not execute 'rocksdb_ratelimiter_create'")
  }
}

class PerfContext(
  private val perfContext: CPointer<rocksdb_perfcontext_t>?
) {
  fun reset(): Unit = 
    rocksdb_perfcontext_reset(perfContext) 

  fun report(
    excludeZeroCounters: Boolean, 
  ): CPointer<ByteVar>? = 
    rocksdb_perfcontext_report(perfContext, excludeZeroCounters.toUByte()) 

  fun metric(
    metric: Int, 
  ): ULong = 
    rocksdb_perfcontext_metric(perfContext, metric) 

  fun destroy(): Unit = 
    rocksdb_perfcontext_destroy(perfContext) 

  companion object {
    fun create(): CPointer<rocksdb_perfcontext_t> = 
      rocksdb_perfcontext_create() 
       ?: error("could not execute 'rocksdb_perfcontext_create'")
  }
}

class CompactionFilter(
  private val compactionFilter: CPointer<rocksdb_compactionfilter_t>?
) {
  fun setIgnoreSnapshots(
    arg1: Boolean, 
  ): Unit = 
    rocksdb_compactionfilter_set_ignore_snapshots(compactionFilter, arg1.toUByte()) 

  fun destroy(): Unit = 
    rocksdb_compactionfilter_destroy(compactionFilter) 

  companion object {
    fun create(
      state: CValuesRef<*>?, 
      destructor: CPointer<CFunction<Function1<COpaquePointer?, Unit>>>?, 
      filter: CPointer<CFunction<Function9<COpaquePointer?, Int, CPointer<ByteVar>?, ULong, CPointer<ByteVar>?, ULong, CPointer<CPointerVarOf<CPointer<ByteVar>>>?, CPointer<ULongVarOf<ULong>>?, CPointer<UByteVarOf<UByte>>?, UByte>>>?, 
      name: CPointer<CFunction<Function1<COpaquePointer?, CPointer<ByteVar>?>>>?, 
    ): CPointer<rocksdb_compactionfilter_t> = 
      rocksdb_compactionfilter_create(state, destructor, filter, name) 
       ?: error("could not execute 'rocksdb_compactionfilter_create'")
  }
}

class CompactionFilterFactory(
  private val compactionFilterFactory: CPointer<rocksdb_compactionfilterfactory_t>?
) {
  fun destroy(): Unit = 
    rocksdb_compactionfilterfactory_destroy(compactionFilterFactory) 

  companion object {
    fun create(
      state: CValuesRef<*>?, 
      destructor: CPointer<CFunction<Function1<COpaquePointer?, Unit>>>?, 
      createCompactionFilter: CPointer<CFunction<Function2<COpaquePointer?, CPointer<rocksdb_compactionfiltercontext_t>?, CPointer<rocksdb_compactionfilter_t>?>>>?, 
      name: CPointer<CFunction<Function1<COpaquePointer?, CPointer<ByteVar>?>>>?, 
    ): CPointer<rocksdb_compactionfilterfactory_t> = 
      rocksdb_compactionfilterfactory_create(state, destructor, createCompactionFilter, name) 
       ?: error("could not execute 'rocksdb_compactionfilterfactory_create'")
  }
}

class Comparator(
  private val comparator: CPointer<rocksdb_comparator_t>?
) {
  fun destroy(): Unit = 
    rocksdb_comparator_destroy(comparator) 

  companion object {
    fun create(
      state: CValuesRef<*>?, 
      destructor: CPointer<CFunction<Function1<COpaquePointer?, Unit>>>?, 
      compare: CPointer<CFunction<Function5<COpaquePointer?, CPointer<ByteVar>?, ULong, CPointer<ByteVar>?, ULong, Int>>>?, 
      name: CPointer<CFunction<Function1<COpaquePointer?, CPointer<ByteVar>?>>>?, 
    ): CPointer<rocksdb_comparator_t> = 
      rocksdb_comparator_create(state, destructor, compare, name) 
       ?: error("could not execute 'rocksdb_comparator_create'")
    
    fun withTsCreate(
      state: CValuesRef<*>?, 
      destructor: CPointer<CFunction<Function1<COpaquePointer?, Unit>>>?, 
      compare: CPointer<CFunction<Function5<COpaquePointer?, CPointer<ByteVar>?, ULong, CPointer<ByteVar>?, ULong, Int>>>?, 
      compareTs: CPointer<CFunction<Function5<COpaquePointer?, CPointer<ByteVar>?, ULong, CPointer<ByteVar>?, ULong, Int>>>?, 
      compareWithoutTs: CPointer<CFunction<Function7<COpaquePointer?, CPointer<ByteVar>?, ULong, UByte, CPointer<ByteVar>?, ULong, UByte, Int>>>?, 
      name: CPointer<CFunction<Function1<COpaquePointer?, CPointer<ByteVar>?>>>?, 
      timestampSize: ULong, 
    ): CPointer<rocksdb_comparator_t> = 
      rocksdb_comparator_with_ts_create(state, destructor, compare, compareTs, compareWithoutTs, name, timestampSize) 
       ?: error("could not execute 'rocksdb_comparator_with_ts_create'")
  }
}

class ComparatorWithTs(
  private val comparatorWithTs: CPointer<rocksdb_comparator_t>?
) {


  companion object {
    fun create(
      state: CValuesRef<*>?, 
      destructor: CPointer<CFunction<Function1<COpaquePointer?, Unit>>>?, 
      compare: CPointer<CFunction<Function5<COpaquePointer?, CPointer<ByteVar>?, ULong, CPointer<ByteVar>?, ULong, Int>>>?, 
      compareTs: CPointer<CFunction<Function5<COpaquePointer?, CPointer<ByteVar>?, ULong, CPointer<ByteVar>?, ULong, Int>>>?, 
      compareWithoutTs: CPointer<CFunction<Function7<COpaquePointer?, CPointer<ByteVar>?, ULong, UByte, CPointer<ByteVar>?, ULong, UByte, Int>>>?, 
      name: CPointer<CFunction<Function1<COpaquePointer?, CPointer<ByteVar>?>>>?, 
      timestampSize: ULong, 
    ): CPointer<rocksdb_comparator_t> = 
      rocksdb_comparator_with_ts_create(state, destructor, compare, compareTs, compareWithoutTs, name, timestampSize) 
       ?: error("could not execute 'rocksdb_comparator_with_ts_create'")
  }
}

class MergeOperator(
  private val mergeOperator: CPointer<rocksdb_mergeoperator_t>?
) {
  fun destroy(): Unit = 
    rocksdb_mergeoperator_destroy(mergeOperator) 

  companion object {
    fun create(
      state: CValuesRef<*>?, 
      destructor: CPointer<CFunction<Function1<COpaquePointer?, Unit>>>?, 
      fullMerge: CPointer<CFunction<Function10<COpaquePointer?, CPointer<ByteVar>?, ULong, CPointer<ByteVar>?, ULong, CPointer<CPointerVarOf<CPointer<ByteVar>>>?, CPointer<ULongVarOf<ULong>>?, Int, CPointer<UByteVarOf<UByte>>?, CPointer<ULongVarOf<ULong>>?, CPointer<ByteVar>?>>>?, 
      partialMerge: CPointer<CFunction<Function8<COpaquePointer?, CPointer<ByteVar>?, ULong, CPointer<CPointerVarOf<CPointer<ByteVar>>>?, CPointer<ULongVarOf<ULong>>?, Int, CPointer<UByteVarOf<UByte>>?, CPointer<ULongVarOf<ULong>>?, CPointer<ByteVar>?>>>?, 
      deleteValue: CPointer<CFunction<Function3<COpaquePointer?, CPointer<ByteVar>?, ULong, Unit>>>?, 
      name: CPointer<CFunction<Function1<COpaquePointer?, CPointer<ByteVar>?>>>?, 
    ): CPointer<rocksdb_mergeoperator_t> = 
      rocksdb_mergeoperator_create(state, destructor, fullMerge, partialMerge, deleteValue, name) 
       ?: error("could not execute 'rocksdb_mergeoperator_create'")
  }
}

class ReadOptions(
  private val readOptions: CPointer<rocksdb_readoptions_t>?
) {
  fun destroy(): Unit = 
    rocksdb_readoptions_destroy(readOptions) 

  fun setSnapshot(
    arg1: CValuesRef<rocksdb_snapshot_t>?, 
  ): Unit = 
    rocksdb_readoptions_set_snapshot(readOptions, arg1) 

  fun setIterateUpperBound(
    key: String?, 
    keyLength: ULong, 
  ): Unit = 
    rocksdb_readoptions_set_iterate_upper_bound(readOptions, key, keyLength) 

  fun setIterateLowerBound(
    key: String?, 
    keyLength: ULong, 
  ): Unit = 
    rocksdb_readoptions_set_iterate_lower_bound(readOptions, key, keyLength) 

  fun setManaged(
    arg1: Boolean, 
  ): Unit = 
    rocksdb_readoptions_set_managed(readOptions, arg1.toUByte()) 

  fun setTimestamp(
    ts: String?, 
    tsLength: ULong, 
  ): Unit = 
    rocksdb_readoptions_set_timestamp(readOptions, ts, tsLength) 

  fun setIterStartTs(
    ts: String?, 
    tsLength: ULong, 
  ): Unit = 
    rocksdb_readoptions_set_iter_start_ts(readOptions, ts, tsLength) 

  var verifyChecksums: Boolean
    get() = rocksdb_readoptions_get_verify_checksums(readOptions).toBoolean()
    set(value) = rocksdb_readoptions_set_verify_checksums(readOptions, value.toUByte())

  var fillCache: Boolean
    get() = rocksdb_readoptions_get_fill_cache(readOptions).toBoolean()
    set(value) = rocksdb_readoptions_set_fill_cache(readOptions, value.toUByte())

  var readTier: Int
    get() = rocksdb_readoptions_get_read_tier(readOptions)
    set(value) = rocksdb_readoptions_set_read_tier(readOptions, value)

  var tailing: Boolean
    get() = rocksdb_readoptions_get_tailing(readOptions).toBoolean()
    set(value) = rocksdb_readoptions_set_tailing(readOptions, value.toUByte())

  var readAheadSize: ULong
    get() = rocksdb_readoptions_get_readahead_size(readOptions)
    set(value) = rocksdb_readoptions_set_readahead_size(readOptions, value)

  var prefixSameAsStart: Boolean
    get() = rocksdb_readoptions_get_prefix_same_as_start(readOptions).toBoolean()
    set(value) = rocksdb_readoptions_set_prefix_same_as_start(readOptions, value.toUByte())

  var pinData: Boolean
    get() = rocksdb_readoptions_get_pin_data(readOptions).toBoolean()
    set(value) = rocksdb_readoptions_set_pin_data(readOptions, value.toUByte())

  var totalOrderSeek: Boolean
    get() = rocksdb_readoptions_get_total_order_seek(readOptions).toBoolean()
    set(value) = rocksdb_readoptions_set_total_order_seek(readOptions, value.toUByte())

  var maxSkippableInternalKeys: ULong
    get() = rocksdb_readoptions_get_max_skippable_internal_keys(readOptions)
    set(value) = rocksdb_readoptions_set_max_skippable_internal_keys(readOptions, value)

  var backgroundPurgeOnIteratorCleanup: Boolean
    get() = rocksdb_readoptions_get_background_purge_on_iterator_cleanup(readOptions).toBoolean()
    set(value) = rocksdb_readoptions_set_background_purge_on_iterator_cleanup(readOptions, value.toUByte())

  var ignoreRangeDeletions: Boolean
    get() = rocksdb_readoptions_get_ignore_range_deletions(readOptions).toBoolean()
    set(value) = rocksdb_readoptions_set_ignore_range_deletions(readOptions, value.toUByte())

  var deadline: ULong
    get() = rocksdb_readoptions_get_deadline(readOptions)
    set(value) = rocksdb_readoptions_set_deadline(readOptions, value)

  var ioTimeout: ULong
    get() = rocksdb_readoptions_get_io_timeout(readOptions)
    set(value) = rocksdb_readoptions_set_io_timeout(readOptions, value)

  companion object {
    fun create(): CPointer<rocksdb_readoptions_t> = 
      rocksdb_readoptions_create() 
       ?: error("could not execute 'rocksdb_readoptions_create'")
  }
}

class WriteOptions(
  private val writeOptions: CPointer<rocksdb_writeoptions_t>?
) {
  fun destroy(): Unit = 
    rocksdb_writeoptions_destroy(writeOptions) 

  fun disableWal(
    disable: Int, 
  ): Unit = 
    rocksdb_writeoptions_disable_WAL(writeOptions, disable) 

  var sync: Boolean
    get() = rocksdb_writeoptions_get_sync(writeOptions).toBoolean()
    set(value) = rocksdb_writeoptions_set_sync(writeOptions, value.toUByte())

  val disableWal: Boolean
    get() = rocksdb_writeoptions_get_disable_WAL(writeOptions).toBoolean()

  var ignoreMissingColumnFamilies: Boolean
    get() = rocksdb_writeoptions_get_ignore_missing_column_families(writeOptions).toBoolean()
    set(value) = rocksdb_writeoptions_set_ignore_missing_column_families(writeOptions, value.toUByte())

  var noSlowdown: Boolean
    get() = rocksdb_writeoptions_get_no_slowdown(writeOptions).toBoolean()
    set(value) = rocksdb_writeoptions_set_no_slowdown(writeOptions, value.toUByte())

  var lowPri: Boolean
    get() = rocksdb_writeoptions_get_low_pri(writeOptions).toBoolean()
    set(value) = rocksdb_writeoptions_set_low_pri(writeOptions, value.toUByte())

  var memtableInsertHintPerBatch: Boolean
    get() = rocksdb_writeoptions_get_memtable_insert_hint_per_batch(writeOptions).toBoolean()
    set(value) = rocksdb_writeoptions_set_memtable_insert_hint_per_batch(writeOptions, value.toUByte())

  companion object {
    fun create(): CPointer<rocksdb_writeoptions_t> = 
      rocksdb_writeoptions_create() 
       ?: error("could not execute 'rocksdb_writeoptions_create'")
  }
}

class CompactOptions(
  private val compactOptions: CPointer<rocksdb_compactoptions_t>?
) {
  fun destroy(): Unit = 
    rocksdb_compactoptions_destroy(compactOptions) 

  fun setFullHistoryTsLow(
    ts: CValuesRef<ByteVar>?, 
    tsLength: ULong, 
  ): Unit = 
    rocksdb_compactoptions_set_full_history_ts_low(compactOptions, ts, tsLength) 

  var exclusiveManualCompaction: Boolean
    get() = rocksdb_compactoptions_get_exclusive_manual_compaction(compactOptions).toBoolean()
    set(value) = rocksdb_compactoptions_set_exclusive_manual_compaction(compactOptions, value.toUByte())

  var bottommostLevelCompaction: Boolean
    get() = rocksdb_compactoptions_get_bottommost_level_compaction(compactOptions).toBoolean()
    set(value) = rocksdb_compactoptions_set_bottommost_level_compaction(compactOptions, value.toUByte())

  var changeLevel: Boolean
    get() = rocksdb_compactoptions_get_change_level(compactOptions).toBoolean()
    set(value) = rocksdb_compactoptions_set_change_level(compactOptions, value.toUByte())

  var targetLevel: Int
    get() = rocksdb_compactoptions_get_target_level(compactOptions)
    set(value) = rocksdb_compactoptions_set_target_level(compactOptions, value)

  companion object {
    fun create(): CPointer<rocksdb_compactoptions_t> = 
      rocksdb_compactoptions_create() 
       ?: error("could not execute 'rocksdb_compactoptions_create'")
  }
}

class FlushOptions(
  private val flushOptions: CPointer<rocksdb_flushoptions_t>?
) {
  fun destroy(): Unit = 
    rocksdb_flushoptions_destroy(flushOptions) 

  var wait: Boolean
    get() = rocksdb_flushoptions_get_wait(flushOptions).toBoolean()
    set(value) = rocksdb_flushoptions_set_wait(flushOptions, value.toUByte())

  companion object {
    fun create(): CPointer<rocksdb_flushoptions_t> = 
      rocksdb_flushoptions_create() 
       ?: error("could not execute 'rocksdb_flushoptions_create'")
  }
}

class JemallocNoDumpAllocator(
  private val jemallocNoDumpAllocator: CPointer<rocksdb_memory_allocator_t>?
) {


  companion object {
    fun create(
      errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
    ): CPointer<rocksdb_memory_allocator_t> = 
      rocksdb_jemalloc_nodump_allocator_create(errorPointer) 
       ?: error("could not execute 'rocksdb_jemalloc_nodump_allocator_create'")
  }
}

class LruCacheOptions(
  private val lruCacheOptions: CPointer<rocksdb_lru_cache_options_t>?
) {
  fun destroy(): Unit = 
    rocksdb_lru_cache_options_destroy(lruCacheOptions) 

  fun setCapacity(
    arg1: ULong, 
  ): Unit = 
    rocksdb_lru_cache_options_set_capacity(lruCacheOptions, arg1) 

  fun setNumShardBits(
    arg1: Int, 
  ): Unit = 
    rocksdb_lru_cache_options_set_num_shard_bits(lruCacheOptions, arg1) 

  fun setMemoryAllocator(
    arg1: CValuesRef<rocksdb_memory_allocator_t>?, 
  ): Unit = 
    rocksdb_lru_cache_options_set_memory_allocator(lruCacheOptions, arg1) 

  companion object {
    fun create(): CPointer<rocksdb_lru_cache_options_t> = 
      rocksdb_lru_cache_options_create() 
       ?: error("could not execute 'rocksdb_lru_cache_options_create'")
  }
}

class DbPath(
  private val dbPath: CPointer<rocksdb_dbpath_t>?
) {
  fun destroy(): Unit = 
    rocksdb_dbpath_destroy(dbPath) 

  companion object {
    fun create(
      path: String?, 
      targetSize: ULong, 
    ): CPointer<rocksdb_dbpath_t> = 
      rocksdb_dbpath_create(path, targetSize) 
       ?: error("could not execute 'rocksdb_dbpath_create'")
  }
}

class EnvOptions(
  private val envOptions: CPointer<rocksdb_envoptions_t>?
) {
  fun destroy(): Unit = 
    rocksdb_envoptions_destroy(envOptions) 

  companion object {
    fun create(): CPointer<rocksdb_envoptions_t> = 
      rocksdb_envoptions_create() 
       ?: error("could not execute 'rocksdb_envoptions_create'")
  }
}

class SstFileWriter(
  private val sstFileWriter: CPointer<rocksdb_sstfilewriter_t>?
) {
  fun open(
    name: String?, 
    errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
  ): Unit = 
    rocksdb_sstfilewriter_open(sstFileWriter, name, errorPointer) 

  fun add(
    key: String?, 
    keyLength: ULong, 
    value: String?, 
    valueLength: ULong, 
    errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
  ): Unit = 
    rocksdb_sstfilewriter_add(sstFileWriter, key, keyLength, value, valueLength, errorPointer) 

  fun put(
    key: String?, 
    keyLength: ULong, 
    value: String?, 
    valueLength: ULong, 
    errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
  ): Unit = 
    rocksdb_sstfilewriter_put(sstFileWriter, key, keyLength, value, valueLength, errorPointer) 

  fun putWithTs(
    key: String?, 
    keyLength: ULong, 
    ts: String?, 
    tsLength: ULong, 
    value: String?, 
    valueLength: ULong, 
    errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
  ): Unit = 
    rocksdb_sstfilewriter_put_with_ts(sstFileWriter, key, keyLength, ts, tsLength, value, valueLength, errorPointer) 

  fun merge(
    key: String?, 
    keyLength: ULong, 
    value: String?, 
    valueLength: ULong, 
    errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
  ): Unit = 
    rocksdb_sstfilewriter_merge(sstFileWriter, key, keyLength, value, valueLength, errorPointer) 

  fun delete(
    key: String?, 
    keyLength: ULong, 
    errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
  ): Unit = 
    rocksdb_sstfilewriter_delete(sstFileWriter, key, keyLength, errorPointer) 

  fun deleteWithTs(
    key: String?, 
    keyLength: ULong, 
    ts: String?, 
    tsLength: ULong, 
    errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
  ): Unit = 
    rocksdb_sstfilewriter_delete_with_ts(sstFileWriter, key, keyLength, ts, tsLength, errorPointer) 

  fun deleteRange(
    beginKey: String?, 
    beginKeyLength: ULong, 
    endKey: String?, 
    endKeyLength: ULong, 
    errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
  ): Unit = 
    rocksdb_sstfilewriter_delete_range(sstFileWriter, beginKey, beginKeyLength, endKey, endKeyLength, errorPointer) 

  fun finish(
    errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
  ): Unit = 
    rocksdb_sstfilewriter_finish(sstFileWriter, errorPointer) 

  fun fileSize(
    fileSize: CValuesRef<ULongVarOf<ULong>>?, 
  ): Unit = 
    rocksdb_sstfilewriter_file_size(sstFileWriter, fileSize) 

  fun destroy(): Unit = 
    rocksdb_sstfilewriter_destroy(sstFileWriter) 

  companion object {
    fun create(
      env: CValuesRef<rocksdb_envoptions_t>?, 
      ioOptions: CValuesRef<rocksdb_options_t>?, 
    ): CPointer<rocksdb_sstfilewriter_t> = 
      rocksdb_sstfilewriter_create(env, ioOptions) 
       ?: error("could not execute 'rocksdb_sstfilewriter_create'")
    
    fun createWithComparator(
      env: CValuesRef<rocksdb_envoptions_t>?, 
      ioOptions: CValuesRef<rocksdb_options_t>?, 
      comparator: CValuesRef<rocksdb_comparator_t>?, 
    ): CPointer<rocksdb_sstfilewriter_t> = 
      rocksdb_sstfilewriter_create_with_comparator(env, ioOptions, comparator) 
       ?: error("could not execute 'rocksdb_sstfilewriter_create_with_comparator'")
  }
}

class IngestExternalFileOptions(
  private val ingestExternalFileOptions: CPointer<rocksdb_ingestexternalfileoptions_t>?
) {
  fun setMoveFiles(
    moveFiles: Boolean, 
  ): Unit = 
    rocksdb_ingestexternalfileoptions_set_move_files(ingestExternalFileOptions, moveFiles.toUByte()) 

  fun setSnapshotConsistency(
    snapshotConsistency: Boolean, 
  ): Unit = 
    rocksdb_ingestexternalfileoptions_set_snapshot_consistency(ingestExternalFileOptions, snapshotConsistency.toUByte()) 

  fun setAllowGlobalSeqno(
    allowGlobalSeqno: Boolean, 
  ): Unit = 
    rocksdb_ingestexternalfileoptions_set_allow_global_seqno(ingestExternalFileOptions, allowGlobalSeqno.toUByte()) 

  fun setAllowBlockingFlush(
    allowBlockingFlush: Boolean, 
  ): Unit = 
    rocksdb_ingestexternalfileoptions_set_allow_blocking_flush(ingestExternalFileOptions, allowBlockingFlush.toUByte()) 

  fun setIngestBehind(
    ingestBehind: Boolean, 
  ): Unit = 
    rocksdb_ingestexternalfileoptions_set_ingest_behind(ingestExternalFileOptions, ingestBehind.toUByte()) 

  fun destroy(): Unit = 
    rocksdb_ingestexternalfileoptions_destroy(ingestExternalFileOptions) 

  companion object {
    fun create(): CPointer<rocksdb_ingestexternalfileoptions_t> = 
      rocksdb_ingestexternalfileoptions_create() 
       ?: error("could not execute 'rocksdb_ingestexternalfileoptions_create'")
  }
}

class SliceTransform(
  private val sliceTransform: CPointer<rocksdb_slicetransform_t>?
) {
  fun destroy(): Unit = 
    rocksdb_slicetransform_destroy(sliceTransform) 

  companion object {
    fun create(
      state: CValuesRef<*>?, 
      destructor: CPointer<CFunction<Function1<COpaquePointer?, Unit>>>?, 
      transform: CPointer<CFunction<Function4<COpaquePointer?, CPointer<ByteVar>?, ULong, CPointer<ULongVarOf<ULong>>?, CPointer<ByteVar>?>>>?, 
      inDomain: CPointer<CFunction<Function3<COpaquePointer?, CPointer<ByteVar>?, ULong, UByte>>>?, 
      inRange: CPointer<CFunction<Function3<COpaquePointer?, CPointer<ByteVar>?, ULong, UByte>>>?, 
      name: CPointer<CFunction<Function1<COpaquePointer?, CPointer<ByteVar>?>>>?, 
    ): CPointer<rocksdb_slicetransform_t> = 
      rocksdb_slicetransform_create(state, destructor, transform, inDomain, inRange, name) 
       ?: error("could not execute 'rocksdb_slicetransform_create'")
    
    fun createFixedPrefix(
      arg0: ULong, 
    ): CPointer<rocksdb_slicetransform_t> = 
      rocksdb_slicetransform_create_fixed_prefix(arg0) 
       ?: error("could not execute 'rocksdb_slicetransform_create_fixed_prefix'")
    
    fun createNoop(): CPointer<rocksdb_slicetransform_t> = 
      rocksdb_slicetransform_create_noop() 
       ?: error("could not execute 'rocksdb_slicetransform_create_noop'")
  }
}

class UniversalCompactionOptions(
  private val universalCompactionOptions: CPointer<rocksdb_universal_compaction_options_t>?
) {
  fun destroy(): Unit = 
    rocksdb_universal_compaction_options_destroy(universalCompactionOptions) 

  var sizeRatio: Int
    get() = rocksdb_universal_compaction_options_get_size_ratio(universalCompactionOptions)
    set(value) = rocksdb_universal_compaction_options_set_size_ratio(universalCompactionOptions, value)

  var minMergeWidth: Int
    get() = rocksdb_universal_compaction_options_get_min_merge_width(universalCompactionOptions)
    set(value) = rocksdb_universal_compaction_options_set_min_merge_width(universalCompactionOptions, value)

  var maxMergeWidth: Int
    get() = rocksdb_universal_compaction_options_get_max_merge_width(universalCompactionOptions)
    set(value) = rocksdb_universal_compaction_options_set_max_merge_width(universalCompactionOptions, value)

  var maxSizeAmplificationPercent: Int
    get() = rocksdb_universal_compaction_options_get_max_size_amplification_percent(universalCompactionOptions)
    set(value) = rocksdb_universal_compaction_options_set_max_size_amplification_percent(universalCompactionOptions, value)

  var compressionSizePercent: Int
    get() = rocksdb_universal_compaction_options_get_compression_size_percent(universalCompactionOptions)
    set(value) = rocksdb_universal_compaction_options_set_compression_size_percent(universalCompactionOptions, value)

  var stopStyle: Int
    get() = rocksdb_universal_compaction_options_get_stop_style(universalCompactionOptions)
    set(value) = rocksdb_universal_compaction_options_set_stop_style(universalCompactionOptions, value)

  companion object {
    fun create(): CPointer<rocksdb_universal_compaction_options_t> = 
      rocksdb_universal_compaction_options_create() 
       ?: error("could not execute 'rocksdb_universal_compaction_options_create'")
  }
}

class FifoCompactionOptions(
  private val fifoCompactionOptions: CPointer<rocksdb_fifo_compaction_options_t>?
) {
  fun destroy(): Unit = 
    rocksdb_fifo_compaction_options_destroy(fifoCompactionOptions) 

  var maxTableFilesSize: ULong
    get() = rocksdb_fifo_compaction_options_get_max_table_files_size(fifoCompactionOptions)
    set(value) = rocksdb_fifo_compaction_options_set_max_table_files_size(fifoCompactionOptions, value)

  companion object {
    fun create(): CPointer<rocksdb_fifo_compaction_options_t> = 
      rocksdb_fifo_compaction_options_create() 
       ?: error("could not execute 'rocksdb_fifo_compaction_options_create'")
  }
}

class TransactionDbCheckpointObject(
  private val transactionDbCheckpointObject: CPointer<rocksdb_checkpoint_t>?
) {


  companion object {
    fun create(
      txnDb: CValuesRef<rocksdb_transactiondb_t>?, 
      errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
    ): CPointer<rocksdb_checkpoint_t> = 
      rocksdb_transactiondb_checkpoint_object_create(txnDb, errorPointer) 
       ?: error("could not execute 'rocksdb_transactiondb_checkpoint_object_create'")
  }
}

class OptimisticTransactionDbCheckpointObject(
  private val optimisticTransactionDbCheckpointObject: CPointer<rocksdb_checkpoint_t>?
) {


  companion object {
    fun create(
      otxnDb: CValuesRef<rocksdb_optimistictransactiondb_t>?, 
      errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
    ): CPointer<rocksdb_checkpoint_t> = 
      rocksdb_optimistictransactiondb_checkpoint_object_create(otxnDb, errorPointer) 
       ?: error("could not execute 'rocksdb_optimistictransactiondb_checkpoint_object_create'")
  }
}

class TransactionDbOptions(
  private val transactionDbOptions: CPointer<rocksdb_transactiondb_options_t>?
) {
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

  companion object {
    fun create(): CPointer<rocksdb_transactiondb_options_t> = 
      rocksdb_transactiondb_options_create() 
       ?: error("could not execute 'rocksdb_transactiondb_options_create'")
  }
}

class TransactionOptions(
  private val transactionOptions: CPointer<rocksdb_transaction_options_t>?
) {
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

  companion object {
    fun create(): CPointer<rocksdb_transaction_options_t> = 
      rocksdb_transaction_options_create() 
       ?: error("could not execute 'rocksdb_transaction_options_create'")
  }
}

class OptimisticTransactionOptions(
  private val optimisticTransactionOptions: CPointer<rocksdb_optimistictransaction_options_t>?
) {
  fun destroy(): Unit = 
    rocksdb_optimistictransaction_options_destroy(optimisticTransactionOptions) 

  fun setSetSnapshot(
    v: Boolean, 
  ): Unit = 
    rocksdb_optimistictransaction_options_set_set_snapshot(optimisticTransactionOptions, v.toUByte()) 

  companion object {
    fun create(): CPointer<rocksdb_optimistictransaction_options_t> = 
      rocksdb_optimistictransaction_options_create() 
       ?: error("could not execute 'rocksdb_optimistictransaction_options_create'")
  }
}

class MemoryConsumers(
  private val memoryConsumers: CPointer<rocksdb_memory_consumers_t>?
) {
  fun addDb(
    db: CValuesRef<rocksdb_t>?, 
  ): Unit = 
    rocksdb_memory_consumers_add_db(memoryConsumers, db) 

  fun addCache(
    cache: CValuesRef<rocksdb_cache_t>?, 
  ): Unit = 
    rocksdb_memory_consumers_add_cache(memoryConsumers, cache) 

  fun destroy(): Unit = 
    rocksdb_memory_consumers_destroy(memoryConsumers) 

  companion object {
    fun create(): CPointer<rocksdb_memory_consumers_t> = 
      rocksdb_memory_consumers_create() 
       ?: error("could not execute 'rocksdb_memory_consumers_create'")
  }
}

class ApproximateMemoryUsage(
  private val approximateMemoryUsage: CPointer<rocksdb_memory_usage_t>?
) {
  fun destroy(): Unit = 
    rocksdb_approximate_memory_usage_destroy(approximateMemoryUsage) 

  val memTableTotal: ULong
    get() = rocksdb_approximate_memory_usage_get_mem_table_total(approximateMemoryUsage)

  val memTableUnflushed: ULong
    get() = rocksdb_approximate_memory_usage_get_mem_table_unflushed(approximateMemoryUsage)

  val memTableReadersTotal: ULong
    get() = rocksdb_approximate_memory_usage_get_mem_table_readers_total(approximateMemoryUsage)

  val cacheTotal: ULong
    get() = rocksdb_approximate_memory_usage_get_cache_total(approximateMemoryUsage)

  companion object {
    fun create(
      consumers: CValuesRef<rocksdb_memory_consumers_t>?, 
      errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
    ): CPointer<rocksdb_memory_usage_t> = 
      rocksdb_approximate_memory_usage_create(consumers, errorPointer) 
       ?: error("could not execute 'rocksdb_approximate_memory_usage_create'")
  }
}
