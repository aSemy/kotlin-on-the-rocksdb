package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class UniversalCompactionOptions(
  private val universalCompactionOptions: CPointer<rocksdb_universal_compaction_options_t> = rocksdb_universal_compaction_options_create() ?: error("could not instantiate new UniversalCompactionOptions")
) : CValuesRef<rocksdb_universal_compaction_options_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_universal_compaction_options_t> =
    universalCompactionOptions.getPointer(scope)

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
}
