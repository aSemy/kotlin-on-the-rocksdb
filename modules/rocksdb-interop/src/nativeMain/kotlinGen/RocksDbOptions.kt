package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class RocksDbOptions(
  private val rocksDbOptions: CPointer<rocksdb_options_t> = rocksdb_options_create() ?: error("could not instantiate new RocksDbOptions")
) : CValuesRef<rocksdb_options_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_options_t> =
    rocksDbOptions.getPointer(scope)

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
}
