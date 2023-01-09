package dev.adamko.kotlin.on.the.rocksdb

import cnames.structs.rocksdb_cache_t
import cnames.structs.rocksdb_compactionfilter_t
import cnames.structs.rocksdb_compactionfilterfactory_t
import cnames.structs.rocksdb_comparator_t
import cnames.structs.rocksdb_env_t
import cnames.structs.rocksdb_options_t
import cnames.structs.rocksdb_universal_compaction_options_t
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.UByte
import kotlin.ULong
import kotlin.Unit
import kotlinx.cinterop.AutofreeScope
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CValuesRef
import org.rocksdb.*

// maybe copy & paste docs from https://github.com/facebook/rocksdb/blob/main/include/rocksdb/options.h?
class RocksDbOptions(
  private val options: CPointer<rocksdb_options_t> = create(),
  configure: RocksDbOptions.() -> Unit = {},
) : CValuesRef<rocksdb_options_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_options_t> =
    options.getPointer(scope)

  init {
    this.configure()
  }

  fun destroy(): Unit = rocksdb_options_destroy(options)
  fun createCopy() = rocksdb_options_create_copy(options)
  fun increaseParallelism(totalThreads: Int) =
    rocksdb_options_increase_parallelism(options, totalThreads)

  fun optimizeForPointLookup(arg1: ULong) = rocksdb_options_optimize_for_point_lookup(options, arg1)
  fun optimizeLevelStyleCompaction(memTableMemoryBudget: ULong) =
    rocksdb_options_optimize_level_style_compaction(options, memTableMemoryBudget)

  fun optimizeUniversalStyleCompaction(memTableMemoryBudget: ULong) =
    rocksdb_options_optimize_universal_style_compaction(options, memTableMemoryBudget)

  fun prepareForBulkLoad() = rocksdb_options_prepare_for_bulk_load(options)
  fun addCompactOnDeletionCollectorFactory(windowSize: ULong, numDeletesTrigger: ULong) =
    rocksdb_options_add_compact_on_deletion_collector_factory(
      options, windowSize, numDeletesTrigger
    )


  fun blobCache(cache: CPointer<rocksdb_cache_t>?) = rocksdb_options_set_blob_cache(options, cache)
  //  fun blockBasedTableFactory() = rocksdb_options_set_block_based_table_factory(options)
//  fun bottommostCompressionOptions() = rocksdb_options_set_bottommost_compression_options(options)
//  fun bottommostCompressionOptionsMaxDictBufferBytes() = rocksdb_options_set_bottommost_compression_options_max_dict_buffer_bytes(options)
//  fun bottommostCompressionOptionsZStdMaxTrainBytes(): Unit = rocksdb_options_set_bottommost_compression_options_zstd_max_train_bytes(options)
  fun compactionFilter(compactionFilter: CPointer<rocksdb_compactionfilter_t>?): Unit =
    rocksdb_options_set_compaction_filter(options, compactionFilter)

  fun compactionFilterFactory(compactionFilterFactory: CPointer<rocksdb_compactionfilterfactory_t>?): Unit =
    rocksdb_options_set_compaction_filter_factory(options, compactionFilterFactory)

  fun comparator(comparator: CPointer<rocksdb_comparator_t>?) =
    rocksdb_options_set_comparator(options, comparator)
  //  fun compressionOptions(compressionOptions: CPointer<rocksdb_compressionOptions_t>?) = rocksdb_options_set_compression_options(options, compressionOptions)
//  fun compressionPerLevel(compressionPerLevel: CPointer<rocksdb_compressionPerLevel_t>?) = rocksdb_options_set_compression_per_level(options, compressionPerLevel)
//  fun cuckooTableFactory(cuckooTableFactory: CPointer<rocksdb_cuckooTableFactory_t>?) = rocksdb_options_set_cuckoo_table_factory(options, cuckooTableFactory)
//  fun dbLogDir(dbLogDir: CPointer<rocksdb_dbLogDir_t>?) = rocksdb_options_set_db_log_dir(options, dbLogDir)
//  fun dbPaths(dbPaths: CPointer<rocksdb_dbPaths_t>?) = rocksdb_options_set_db_paths(options, dbPaths)
//  fun dumpMallocStats(dumpMallocStats: CPointer<rocksdb_dumpMallocStats_t>?) = rocksdb_options_set_dump_malloc_stats(options, dumpMallocStats)
  fun env(env: CPointer<rocksdb_env_t>?) = rocksdb_options_set_env(options, env)
//  fun fifoCompactionOptions(fifoCompactionOptions: CPointer<rocksdb_fifoCompactionOptions_t>?) = rocksdb_options_set_fifo_compaction_options(options, fifoCompactionOptions)
//  fun hashLinkListRep(hashLinkListRep: CPointer<rocksdb_hashLinkListRep_t>?) = rocksdb_options_set_hash_link_list_rep(options, hashLinkListRep)
//  fun hashSkipListRep(hashSkipListRep: CPointer<rocksdb_hashSkipListRep_t>?) = rocksdb_options_set_hash_skip_list_rep(options, hashSkipListRep)
//  fun infoLog(infoLog: CPointer<rocksdb_infoLog_t>?) = rocksdb_options_set_info_log(options, infoLog)
//  fun maxBytesForLevelMultiplierAdditional(maxBytesForLevelMultiplierAdditional: CPointer<rocksdb_maxBytesForLevelMultiplierAdditional_t>?) = rocksdb_options_set_max_bytes_for_level_multiplier_additional(options, maxBytesForLevelMultiplierAdditional)

  fun memTableVectorRep() = rocksdb_options_set_memtable_vector_rep(options)
  //  fun memTableWholeKeyFiltering() = rocksdb_options_set_memtable_whole_key_filtering(options)
//  fun mergeOperator() = rocksdb_options_set_merge_operator(options)
//  fun minLevelToCompress() = rocksdb_options_set_min_level_to_compress(options)
//  fun plainTableFactory() = rocksdb_options_set_plain_table_factory(options)
//  fun prefixExtractor() = rocksdb_options_set_prefix_extractor(options)
//  fun rateLimiter() = rocksdb_options_set_ratelimiter(options)
//  fun rowCache() = rocksdb_options_set_row_cache(options)
  fun uint64addMergeOperator() = rocksdb_options_set_uint64add_merge_operator(options)

  fun universalCompactionOptions(
    compactionOptions: CValuesRef<rocksdb_universal_compaction_options_t>
  ) = rocksdb_options_set_universal_compaction_options(options, compactionOptions)

  fun walDir(dir: String) = rocksdb_options_set_wal_dir(options, dir)

  var compactionReadAheadSize: ULong
    get() = rocksdb_options_get_compaction_readahead_size(options)
    set(value) = rocksdb_options_compaction_readahead_size(options, value)

  var walSizeLimitMB
    get() = rocksdb_options_get_WAL_size_limit_MB(options)
    set(value) = rocksdb_options_set_WAL_size_limit_MB(options, value)

  var wallTlSeconds
    get() = rocksdb_options_get_WAL_ttl_seconds(options)
    set(value) = rocksdb_options_set_WAL_ttl_seconds(options, value)

  var accessHintOnCompactionStart
    get() = rocksdb_options_get_access_hint_on_compaction_start(options)
    set(value) = rocksdb_options_set_access_hint_on_compaction_start(options, value)

  var adviseRandomOnOpen
    get() = rocksdb_options_get_advise_random_on_open(options)
    set(value) = rocksdb_options_set_advise_random_on_open(options, value)

  var allowConcurrentMemTableWrite: UByte
    get() = rocksdb_options_get_allow_concurrent_memtable_write(options)
    set(value) = rocksdb_options_set_allow_concurrent_memtable_write(options, value)

  var allowIngestBehind: Boolean
    get() = rocksdb_options_get_allow_ingest_behind(options).toBoolean();
    set(value) = rocksdb_options_set_allow_ingest_behind(options, value.toUByte())

  var allowMmapReads
    get() = rocksdb_options_get_allow_mmap_reads(options)
    set(value) = rocksdb_options_set_allow_mmap_reads(options, value)

  var allowMmapWrites
    get() = rocksdb_options_get_allow_mmap_writes(options)
    set(value) = rocksdb_options_set_allow_mmap_writes(options, value)

  var arenaBlockSize
    get() = rocksdb_options_get_arena_block_size(options)
    set(value) = rocksdb_options_set_arena_block_size(options, value)

  var atomicFlush
    get() = rocksdb_options_get_atomic_flush(options)
    set(value) = rocksdb_options_set_atomic_flush(options, value)

  var avoidUnnecessaryBlockingIo
    get() = rocksdb_options_get_avoid_unnecessary_blocking_io(options)
    set(value) = rocksdb_options_set_avoid_unnecessary_blocking_io(options, value)

  var blobCompactionReadAheadSize
    get() = rocksdb_options_get_blob_compaction_readahead_size(options)
    set(value) = rocksdb_options_set_blob_compaction_readahead_size(options, value)

  var blobCompressionType
    get() = rocksdb_options_get_blob_compression_type(options)
    set(value) = rocksdb_options_set_blob_compression_type(options, value)

  var blobFileSize
    get() = rocksdb_options_get_blob_file_size(options)
    set(value) = rocksdb_options_set_blob_file_size(options, value)

  var blobFileStartingLevel
    get() = rocksdb_options_get_blob_file_starting_level(options)
    set(value) = rocksdb_options_set_blob_file_starting_level(options, value)

  var blobGcAgeCutoff
    get() = rocksdb_options_get_blob_gc_age_cutoff(options)
    set(value) = rocksdb_options_set_blob_gc_age_cutoff(options, value)

  var blobGcForceThreshold
    get() = rocksdb_options_get_blob_gc_force_threshold(options)
    set(value) = rocksdb_options_set_blob_gc_force_threshold(options, value)

  var bloomLocality
    get() = rocksdb_options_get_bloom_locality(options)
    set(value) = rocksdb_options_set_bloom_locality(options, value)

  var bottommostCompression
    get() = rocksdb_options_get_bottommost_compression(options)
    set(value) = rocksdb_options_set_bottommost_compression(options, value)

  val bottommostCompressionOptionsUseZStdDictTrainer
    get() = rocksdb_options_get_bottommost_compression_options_use_zstd_dict_trainer(
      options
    )

  fun bottommostCompressionOptionsUseZStdDictTrainer(arg1: Boolean, arg2: Boolean) =
    rocksdb_options_set_bottommost_compression_options_use_zstd_dict_trainer(
      options,
      arg1.toUByte(), arg2.toUByte(),
    )

  var bytesPerSync: ULong
    get() = rocksdb_options_get_bytes_per_sync(options)
    set(value) = rocksdb_options_set_bytes_per_sync(options, value)

  var compactionStyle: Int
    get() = rocksdb_options_get_compaction_style(options)
    set(value) = rocksdb_options_set_compaction_style(options, value)

  var compression: Int
    get() = rocksdb_options_get_compression(options)
    set(value) = rocksdb_options_set_compression(options, value)

  var compressionOptionsMaxDictBufferBytes: ULong
    get() = rocksdb_options_get_compression_options_max_dict_buffer_bytes(options)
    set(value) = rocksdb_options_set_compression_options_max_dict_buffer_bytes(options, value)

  var compressionOptionsParallelThreads
    get() = rocksdb_options_get_compression_options_parallel_threads(options)
    set(value) = rocksdb_options_set_compression_options_parallel_threads(options, value)

  var compressionOptionsUseZStdDictTrainer
    get() = rocksdb_options_get_compression_options_use_zstd_dict_trainer(options)
    set(value) = rocksdb_options_set_compression_options_use_zstd_dict_trainer(options, value)

  var compressionOptionsZStdMaxTrainBytes
    get() = rocksdb_options_get_compression_options_zstd_max_train_bytes(options)
    set(value) = rocksdb_options_set_compression_options_zstd_max_train_bytes(options, value)

  var createIfMissing: Boolean
    get() = rocksdb_options_get_create_if_missing(options).toBoolean()
    set(value) = rocksdb_options_set_create_if_missing(options, value.toUByte())

  var createMissingColumnFamilies
    get() = rocksdb_options_get_create_missing_column_families(options)
    set(value) = rocksdb_options_set_create_missing_column_families(options, value)

  var dbWriteBufferSize
    get() = rocksdb_options_get_db_write_buffer_size(options)
    set(value) = rocksdb_options_set_db_write_buffer_size(options, value)

  var deleteObsoleteFilesPeriodMicros
    get() = rocksdb_options_get_delete_obsolete_files_period_micros(options)
    set(value) = rocksdb_options_set_delete_obsolete_files_period_micros(options, value)

//  var disableAutoCompactions: Int // set/get different types, might be a bug?
//    get() = rocksdb_options_get_disable_auto_compactions(options)
//    set(value) = rocksdb_options_set_disable_auto_compactions(options, value)

  var enableBlobFiles
    get() = rocksdb_options_get_enable_blob_files(options)
    set(value) = rocksdb_options_set_enable_blob_files(options, value)

  var enableBlobGc
    get() = rocksdb_options_get_enable_blob_gc(options)
    set(value) = rocksdb_options_set_enable_blob_gc(options, value)

  var enablePipelinedWrite
    get() = rocksdb_options_get_enable_pipelined_write(options)
    set(value) = rocksdb_options_set_enable_pipelined_write(options, value)

  var enableWriteThreadAdaptiveYield
    get() = rocksdb_options_get_enable_write_thread_adaptive_yield(options)
    set(value) = rocksdb_options_set_enable_write_thread_adaptive_yield(options, value)

  var errorIfExists
    get() = rocksdb_options_get_error_if_exists(options)
    set(value) = rocksdb_options_set_error_if_exists(options, value)

  var experimentalMemPurgeThreshold
    get() = rocksdb_options_get_experimental_mempurge_threshold(options)
    set(value) = rocksdb_options_set_experimental_mempurge_threshold(options, value)

  var hardPendingCompactionBytesLimit
    get() = rocksdb_options_get_hard_pending_compaction_bytes_limit(options)
    set(value) = rocksdb_options_set_hard_pending_compaction_bytes_limit(options, value)

  var infoLogLevel
    get() = rocksdb_options_get_info_log_level(options)
    set(value) = rocksdb_options_set_info_log_level(options, value)

  var inplaceUpdateNumLocks
    get() = rocksdb_options_get_inplace_update_num_locks(options)
    set(value) = rocksdb_options_set_inplace_update_num_locks(options, value)

  var inplaceUpdateSupport
    get() = rocksdb_options_get_inplace_update_support(options)
    set(value) = rocksdb_options_set_inplace_update_support(options, value)

  var isFdCloseOnExec
    get() = rocksdb_options_get_is_fd_close_on_exec(options)
    set(value) = rocksdb_options_set_is_fd_close_on_exec(options, value)

  var keepLogFileNum
    get() = rocksdb_options_get_keep_log_file_num(options)
    set(value) = rocksdb_options_set_keep_log_file_num(options, value)

  var level0FileNumCompactionTrigger
    get() = rocksdb_options_get_level0_file_num_compaction_trigger(options)
    set(value) = rocksdb_options_set_level0_file_num_compaction_trigger(options, value)

  var level0SlowdownWritesTrigger
    get() = rocksdb_options_get_level0_slowdown_writes_trigger(options)
    set(value) = rocksdb_options_set_level0_slowdown_writes_trigger(options, value)

  var level0StopWritesTrigger
    get() = rocksdb_options_get_level0_stop_writes_trigger(options)
    set(value) = rocksdb_options_set_level0_stop_writes_trigger(options, value)

  var levelCompactionDynamicLevelBytes
    get() = rocksdb_options_get_level_compaction_dynamic_level_bytes(options)
    set(value) = rocksdb_options_set_level_compaction_dynamic_level_bytes(options, value)

  var logFileTimeToRoll
    get() = rocksdb_options_get_log_file_time_to_roll(options)
    set(value) = rocksdb_options_set_log_file_time_to_roll(options, value)

  var manifestPreallocationSize
    get() = rocksdb_options_get_manifest_preallocation_size(options)
    set(value) = rocksdb_options_set_manifest_preallocation_size(options, value)

  var manualWalFlush
    get() = rocksdb_options_get_manual_wal_flush(options)
    set(value) = rocksdb_options_set_manual_wal_flush(options, value)

  var maxBackgroundCompactions
    get() = rocksdb_options_get_max_background_compactions(options)
    set(value) = rocksdb_options_set_max_background_compactions(options, value)

  var maxBackgroundFlushes
    get() = rocksdb_options_get_max_background_flushes(options)
    set(value) = rocksdb_options_set_max_background_flushes(options, value)

  var maxBackgroundJobs
    get() = rocksdb_options_get_max_background_jobs(options)
    set(value) = rocksdb_options_set_max_background_jobs(options, value)

  var maxBytesForLevelBase
    get() = rocksdb_options_get_max_bytes_for_level_base(options)
    set(value) = rocksdb_options_set_max_bytes_for_level_base(options, value)

  var maxBytesForLevelMultiplier
    get() = rocksdb_options_get_max_bytes_for_level_multiplier(options)
    set(value) = rocksdb_options_set_max_bytes_for_level_multiplier(options, value)

  var maxCompactionBytes
    get() = rocksdb_options_get_max_compaction_bytes(options)
    set(value) = rocksdb_options_set_max_compaction_bytes(options, value)

  var maxFileOpeningThreads
    get() = rocksdb_options_get_max_file_opening_threads(options)
    set(value) = rocksdb_options_set_max_file_opening_threads(options, value)

  var maxLogFileSize
    get() = rocksdb_options_get_max_log_file_size(options)
    set(value) = rocksdb_options_set_max_log_file_size(options, value)

  var maxManifestFileSize
    get() = rocksdb_options_get_max_manifest_file_size(options)
    set(value) = rocksdb_options_set_max_manifest_file_size(options, value)

  var maxOpenFiles
    get() = rocksdb_options_get_max_open_files(options)
    set(value) = rocksdb_options_set_max_open_files(options, value)

  var maxSequentialSkipInIterations
    get() = rocksdb_options_get_max_sequential_skip_in_iterations(options)
    set(value) = rocksdb_options_set_max_sequential_skip_in_iterations(options, value)

  var maxSubCompactions
    get() = rocksdb_options_get_max_subcompactions(options)
    set(value) = rocksdb_options_set_max_subcompactions(options, value)

  var maxSuccessiveMerges
    get() = rocksdb_options_get_max_successive_merges(options)
    set(value) = rocksdb_options_set_max_successive_merges(options, value)

  var maxTotalWalSize
    get() = rocksdb_options_get_max_total_wal_size(options)
    set(value) = rocksdb_options_set_max_total_wal_size(options, value)

  var maxWriteBufferNumber
    get() = rocksdb_options_get_max_write_buffer_number(options)
    set(value) = rocksdb_options_set_max_write_buffer_number(options, value)

  var maxWriteBufferNumberToMaintain
    get() = rocksdb_options_get_max_write_buffer_number_to_maintain(options)
    set(value) = rocksdb_options_set_max_write_buffer_number_to_maintain(options, value)

  var maxWriteBufferSizeToMaintain
    get() = rocksdb_options_get_max_write_buffer_size_to_maintain(options)
    set(value) = rocksdb_options_set_max_write_buffer_size_to_maintain(options, value)

  var memTableHugePageSize
    get() = rocksdb_options_get_memtable_huge_page_size(options)
    set(value) = rocksdb_options_set_memtable_huge_page_size(options, value)

  var memTablePrefixBloomSizeRatio
    get() = rocksdb_options_get_memtable_prefix_bloom_size_ratio(options)
    set(value) = rocksdb_options_set_memtable_prefix_bloom_size_ratio(options, value)

  var minBlobSize
    get() = rocksdb_options_get_min_blob_size(options)
    set(value) = rocksdb_options_set_min_blob_size(options, value)

  var minWriteBufferNumberToMerge
    get() = rocksdb_options_get_min_write_buffer_number_to_merge(options)
    set(value) = rocksdb_options_set_min_write_buffer_number_to_merge(options, value)

  var numLevels
    get() = rocksdb_options_get_num_levels(options)
    set(value) = rocksdb_options_set_num_levels(options, value)

//  var optimizeFiltersForHits: Int
//    get() = rocksdb_options_get_optimize_filters_for_hits(options)
//    set(value) = rocksdb_options_set_optimize_filters_for_hits(options, value)

  var paranoidChecks
    get() = rocksdb_options_get_paranoid_checks(options)
    set(value) = rocksdb_options_set_paranoid_checks(options, value)

  var prePopulateBlobCache
    get() = rocksdb_options_get_prepopulate_blob_cache(options)
    set(value) = rocksdb_options_set_prepopulate_blob_cache(options, value)

  var recycleLogFileNum
    get() = rocksdb_options_get_recycle_log_file_num(options)
    set(value) = rocksdb_options_set_recycle_log_file_num(options, value)

//  var reportBgIoStats: Int
//    get() = rocksdb_options_get_report_bg_io_stats(options)
//    set(value) = rocksdb_options_set_report_bg_io_stats(options, value)

  var skipCheckingSstFileSizesOnDbOpen
    get() = rocksdb_options_get_skip_checking_sst_file_sizes_on_db_open(options)
    set(value) = rocksdb_options_set_skip_checking_sst_file_sizes_on_db_open(options, value)

  var skipStatsUpdateOnDbOpen
    get() = rocksdb_options_get_skip_stats_update_on_db_open(options)
    set(value) = rocksdb_options_set_skip_stats_update_on_db_open(options, value)

  var softPendingCompactionBytesLimit
    get() = rocksdb_options_get_soft_pending_compaction_bytes_limit(options)
    set(value) = rocksdb_options_set_soft_pending_compaction_bytes_limit(options, value)

  var statsDumpPeriodSec
    get() = rocksdb_options_get_stats_dump_period_sec(options)
    set(value) = rocksdb_options_set_stats_dump_period_sec(options, value)

  var statsPersistPeriodSec
    get() = rocksdb_options_get_stats_persist_period_sec(options)
    set(value) = rocksdb_options_set_stats_persist_period_sec(options, value)

  var tableCacheNumShardBits
    get() = rocksdb_options_get_table_cache_numshardbits(options)
    set(value) = rocksdb_options_set_table_cache_numshardbits(options, value)

  var targetFileSizeBase
    get() = rocksdb_options_get_target_file_size_base(options)
    set(value) = rocksdb_options_set_target_file_size_base(options, value)

  var targetFileSizeMultiplier
    get() = rocksdb_options_get_target_file_size_multiplier(options)
    set(value) = rocksdb_options_set_target_file_size_multiplier(options, value)

  var unorderedWrite
    get() = rocksdb_options_get_unordered_write(options)
    set(value) = rocksdb_options_set_unordered_write(options, value)

  var useAdaptiveMutex
    get() = rocksdb_options_get_use_adaptive_mutex(options)
    set(value) = rocksdb_options_set_use_adaptive_mutex(options, value)

  var useDirectIoForFlushAndCompaction
    get() = rocksdb_options_get_use_direct_io_for_flush_and_compaction(options)
    set(value) = rocksdb_options_set_use_direct_io_for_flush_and_compaction(options, value)

  var useDirectReads
    get() = rocksdb_options_get_use_direct_reads(options)
    set(value) = rocksdb_options_set_use_direct_reads(options, value)

  var useFsync
    get() = rocksdb_options_get_use_fsync(options)
    set(value) = rocksdb_options_set_use_fsync(options, value)

  var walBytesPerSync
    get() = rocksdb_options_get_wal_bytes_per_sync(options)
    set(value) = rocksdb_options_set_wal_bytes_per_sync(options, value)
  var walCompression
    get() = rocksdb_options_get_wal_compression(options)
    set(value) = rocksdb_options_set_wal_compression(options, value)

  var walRecoveryMode
    get() = rocksdb_options_get_wal_recovery_mode(options)
    set(value) = rocksdb_options_set_wal_recovery_mode(options, value)

  var writableFileMaxBufferSize
    get() = rocksdb_options_get_writable_file_max_buffer_size(options)
    set(value) = rocksdb_options_set_writable_file_max_buffer_size(options, value)

  var writeBufferSize: ULong
    get() = rocksdb_options_get_write_buffer_size(options)
    set(value) = rocksdb_options_set_write_buffer_size(options, value)


  companion object {
    fun create(): CPointer<rocksdb_options_t> =
      rocksdb_options_create() ?: error("could not create RocksDbOptions")
  }
}
