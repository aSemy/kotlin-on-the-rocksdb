package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class WriteBatch(
  private val writeBatch: CPointer<rocksdb_writebatch_t> = rocksdb_writebatch_create() ?: error("could not instantiate new WriteBatch")
) : CValuesRef<rocksdb_writebatch_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_writebatch_t> =
    writeBatch.getPointer(scope)

  constructor(
    rep: String?, 
    size: ULong, 
  ): this(rocksdb_writebatch_create_from(rep, size) ?: error("could not instantiate new WriteBatch"))

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

  fun delete(
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
}
