package dev.adamko.kotlin.on.the.rocksdb

//import cnames.structs.rocksdb_writeoptions_t
//import kotlinx.cinterop.AutofreeScope
//import kotlinx.cinterop.CPointer
//import kotlinx.cinterop.CValuesRef
//import org.rocksdb.*
//
//class RocksDbWriteOptions(
//  val options: CPointer<rocksdb_writeoptions_t> = create(),
//  configure: RocksDbWriteOptions.() -> Unit = {},
//) : CValuesRef<rocksdb_writeoptions_t>() {
//
//  init {
//    this.configure()
//  }
//
//  fun destroy(): Unit = rocksdb_writeoptions_destroy(options)
//
//  var disableWAL: Boolean
//    get() = rocksdb_writeoptions_get_disable_WAL(options).toBoolean()
//    set(value) = rocksdb_writeoptions_disable_WAL(options, if (value) 1 else 0)
//
//  var lowPri: Boolean
//    get() = rocksdb_writeoptions_get_low_pri(options).toBoolean()
//    set(value) = rocksdb_writeoptions_set_low_pri(options, value.toUByte())
//
//  var noSlowdown: Boolean
//    get() = rocksdb_writeoptions_get_no_slowdown(options).toBoolean()
//    set(value) = rocksdb_writeoptions_set_no_slowdown(options, value.toUByte())
//
//  var memTableInsertHintPerBatch: Boolean
//    get() = rocksdb_writeoptions_get_memtable_insert_hint_per_batch(options).toBoolean()
//    set(value) = rocksdb_writeoptions_set_memtable_insert_hint_per_batch(options, value.toUByte())
//
//  var sync: Boolean
//    get() = rocksdb_writeoptions_get_sync(options).toBoolean()
//    set(value) = rocksdb_writeoptions_set_sync(options, value.toUByte())
//
//  var ignoreMissingColumnFamilies: Boolean
//    get() = rocksdb_writeoptions_get_ignore_missing_column_families(options).toBoolean()
//    set(value) = rocksdb_writeoptions_set_ignore_missing_column_families(options, value.toUByte())
//
//  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_writeoptions_t> =
//    options.getPointer(scope)
//
//  companion object {
//    fun create(): CPointer<rocksdb_writeoptions_t> =
//      rocksdb_writeoptions_create() ?: error("could not create RocksDbWriteOptions")
//
//  }
//}
