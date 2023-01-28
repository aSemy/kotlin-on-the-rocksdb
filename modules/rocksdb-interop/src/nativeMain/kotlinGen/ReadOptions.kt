package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class ReadOptions(
  private val readOptions: CPointer<rocksdb_readoptions_t> = rocksdb_readoptions_create() 
     ?: error("could not instantiate new ReadOptions")
) : CValuesRef<rocksdb_readoptions_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_readoptions_t> =
    readOptions.getPointer(scope)

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
}
