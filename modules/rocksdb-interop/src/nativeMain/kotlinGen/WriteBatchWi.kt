package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class WriteBatchWi(
  private val writeBatchWi: CPointer<rocksdb_writebatch_wi_t>
) : CValuesRef<rocksdb_writebatch_wi_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_writebatch_wi_t> =
    writeBatchWi.getPointer(scope)

  constructor(
    reservedBytes: ULong,
    overwriteKeys: Boolean,
  ): this(
    rocksdb_writebatch_wi_create(reservedBytes, overwriteKeys.toUByte()) 
      ?: error("could not instantiate new WriteBatchWi")
  )
  
  constructor(
    rep: String?,
    size: ULong,
  ): this(
    rocksdb_writebatch_wi_create_from(rep, size) 
      ?: error("could not instantiate new WriteBatchWi")
  )

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

  fun delete(
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
}
