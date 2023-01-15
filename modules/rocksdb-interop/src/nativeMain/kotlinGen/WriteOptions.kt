package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class WriteOptions(
  private val writeOptions: CPointer<rocksdb_writeoptions_t> = rocksdb_writeoptions_create() ?: error("could not instantiate new WriteOptions")
) : CValuesRef<rocksdb_writeoptions_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_writeoptions_t> =
    writeOptions.getPointer(scope)

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
}
