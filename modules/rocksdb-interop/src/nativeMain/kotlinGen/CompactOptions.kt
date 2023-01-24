package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class CompactOptions(
  private val compactOptions: CPointer<rocksdb_compactoptions_t> = rocksdb_compactoptions_create() ?: error("could not instantiate new CompactOptions")
) : CValuesRef<rocksdb_compactoptions_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_compactoptions_t> =
    compactOptions.getPointer(scope)

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
}
