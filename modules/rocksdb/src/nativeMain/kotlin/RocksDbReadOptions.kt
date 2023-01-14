package dev.adamko.kotlin.on.the.rocksdb
//
//import cnames.structs.rocksdb_readoptions_t
//import cnames.structs.rocksdb_snapshot_t
//import kotlinx.cinterop.AutofreeScope
//import kotlinx.cinterop.CPointer
//import kotlinx.cinterop.CValuesRef
//import kotlinx.cinterop.convert
//import org.rocksdb.*
//
//class RocksDbReadOptions(
//  val options: CPointer<rocksdb_readoptions_t> = create(),
//  configure: RocksDbReadOptions.() -> Unit = {},
//) : CValuesRef<rocksdb_readoptions_t>() {
//
//  fun destroy(): Unit = rocksdb_readoptions_destroy(options)
//
//  init {
//    this.configure()
//  }
//
//  var fillCache: Boolean
//    get() = rocksdb_readoptions_get_fill_cache(options).toBoolean()
//    set(value) = rocksdb_readoptions_set_fill_cache(options, value.toUByte())
//
//  fun backgroundPurgeOnIteratorCleanup(value: Boolean) =
//    rocksdb_readoptions_set_background_purge_on_iterator_cleanup(options, value.toUByte())
//
//  fun deadline(microseconds: ULong) =
//    rocksdb_readoptions_set_deadline(options, microseconds)
//
//  fun ignoreRangeDeletions(value: Boolean) =
//    rocksdb_readoptions_set_ignore_range_deletions(options, value.toUByte())
//
//  fun ioTimeout(microseconds: ULong) =
//    rocksdb_readoptions_set_io_timeout(options, microseconds)
//
//  fun iterStartTs(value: String) =
//    rocksdb_readoptions_set_iter_start_ts(options, value, value.length.convert())
//
//  var skippableInternalKeys: ULong
//    get() = rocksdb_readoptions_get_max_skippable_internal_keys(options)
//    set(value) = rocksdb_readoptions_set_max_skippable_internal_keys(options, value.convert())
//
//  var pinData: Boolean
//    get() = rocksdb_readoptions_get_pin_data(options).toBoolean()
//    set(value) = rocksdb_readoptions_set_pin_data(options, value.toUByte())
//
//  var readTier: Int
//    get() = rocksdb_readoptions_get_read_tier(options)
//    set(value) = rocksdb_readoptions_set_read_tier(options, value)
//
//
//  fun prefixSameAsStart(value: Boolean): Unit =
//    rocksdb_readoptions_set_prefix_same_as_start(options, value.toUByte())
//
//  fun snapshot(
//    snapshot: CValuesRef<rocksdb_snapshot_t>?,
//  ): Unit =
//    rocksdb_readoptions_set_snapshot(options, snapshot)
//
//  fun managed(
//    managed: Boolean
//  ): Unit = rocksdb_readoptions_set_managed(options, managed.toUByte())
//
//  var tailing: Boolean
//    get() = rocksdb_readoptions_get_tailing(options).toBoolean()
//    set(value) = rocksdb_readoptions_set_tailing(options, value.toUByte())
//
//
//  fun integrateLowerBound(value: String) =
//    rocksdb_readoptions_set_iterate_lower_bound(options, value, value.length.convert())
//
//  fun integrateUpperBound(value: String) =
//    rocksdb_readoptions_set_iterate_upper_bound(options, value, value.length.convert())
//
//
//  fun timestamp(
//    value: String
//  ): Unit = rocksdb_readoptions_set_timestamp(options, value, value.length.convert())
//
//  fun readAheadSize(
//    value: ULong
//  ): Unit = rocksdb_readoptions_set_readahead_size(options, value)
//
//  fun totalOrderSeek(
//    value: Boolean
//  ): Unit = rocksdb_readoptions_set_total_order_seek(options, value.toUByte())
//
//
//  fun verifyChecksums(
//    value: Boolean
//  ): Unit = rocksdb_readoptions_set_verify_checksums(options, value.toUByte())
//
//
//  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_readoptions_t> =
//    options.getPointer(scope)
//
//  companion object {
//    fun create(): CPointer<rocksdb_readoptions_t> =
//      rocksdb_readoptions_create() ?: error("could not create RocksDbWriteOptions")
//  }
//}
