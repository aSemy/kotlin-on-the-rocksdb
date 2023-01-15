package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class BlockBasedOptions(
  private val blockBasedOptions: CPointer<rocksdb_block_based_table_options_t> = rocksdb_block_based_options_create() ?: error("could not instantiate new BlockBasedOptions")
) : CValuesRef<rocksdb_block_based_table_options_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_block_based_table_options_t> =
    blockBasedOptions.getPointer(scope)

  fun destroy(): Unit = 
    rocksdb_block_based_options_destroy(blockBasedOptions) 

  fun setChecksum(
    arg1: Byte, 
  ): Unit = 
    rocksdb_block_based_options_set_checksum(blockBasedOptions, arg1) 

  fun setBlockSize(
    blockSize: ULong, 
  ): Unit = 
    rocksdb_block_based_options_set_block_size(blockBasedOptions, blockSize) 

  fun setBlockSizeDeviation(
    blockSizeDeviation: Int, 
  ): Unit = 
    rocksdb_block_based_options_set_block_size_deviation(blockBasedOptions, blockSizeDeviation) 

  fun setBlockRestartInterval(
    blockRestartInterval: Int, 
  ): Unit = 
    rocksdb_block_based_options_set_block_restart_interval(blockBasedOptions, blockRestartInterval) 

  fun setIndexBlockRestartInterval(
    indexBlockRestartInterval: Int, 
  ): Unit = 
    rocksdb_block_based_options_set_index_block_restart_interval(blockBasedOptions, indexBlockRestartInterval) 

  fun setMetadataBlockSize(
    metadataBlockSize: ULong, 
  ): Unit = 
    rocksdb_block_based_options_set_metadata_block_size(blockBasedOptions, metadataBlockSize) 

  fun setPartitionFilters(
    partitionFilters: Boolean, 
  ): Unit = 
    rocksdb_block_based_options_set_partition_filters(blockBasedOptions, partitionFilters.toUByte()) 

  fun setUseDeltaEncoding(
    useDeltaEncoding: Boolean, 
  ): Unit = 
    rocksdb_block_based_options_set_use_delta_encoding(blockBasedOptions, useDeltaEncoding.toUByte()) 

  fun setFilterPolicy(
    filterPolicy: CValuesRef<rocksdb_filterpolicy_t>?, 
  ): Unit = 
    rocksdb_block_based_options_set_filter_policy(blockBasedOptions, filterPolicy) 

  fun setNoBlockCache(
    noBlockCache: Boolean, 
  ): Unit = 
    rocksdb_block_based_options_set_no_block_cache(blockBasedOptions, noBlockCache.toUByte()) 

  fun setBlockCache(
    blockCache: CValuesRef<rocksdb_cache_t>?, 
  ): Unit = 
    rocksdb_block_based_options_set_block_cache(blockBasedOptions, blockCache) 

  fun setBlockCacheCompressed(
    blockCacheCompressed: CValuesRef<rocksdb_cache_t>?, 
  ): Unit = 
    rocksdb_block_based_options_set_block_cache_compressed(blockBasedOptions, blockCacheCompressed) 

  fun setWholeKeyFiltering(
    arg1: Boolean, 
  ): Unit = 
    rocksdb_block_based_options_set_whole_key_filtering(blockBasedOptions, arg1.toUByte()) 

  fun setFormatVersion(
    arg1: Int, 
  ): Unit = 
    rocksdb_block_based_options_set_format_version(blockBasedOptions, arg1) 

  fun setIndexType(
    arg1: Int, 
  ): Unit = 
    rocksdb_block_based_options_set_index_type(blockBasedOptions, arg1) 

  fun setDataBlockIndexType(
    arg1: Int, 
  ): Unit = 
    rocksdb_block_based_options_set_data_block_index_type(blockBasedOptions, arg1) 

  fun setDataBlockHashRatio(
    v: Double, 
  ): Unit = 
    rocksdb_block_based_options_set_data_block_hash_ratio(blockBasedOptions, v) 

  fun setCacheIndexAndFilterBlocks(
    arg1: Boolean, 
  ): Unit = 
    rocksdb_block_based_options_set_cache_index_and_filter_blocks(blockBasedOptions, arg1.toUByte()) 

  fun setCacheIndexAndFilterBlocksWithHighPriority(
    arg1: Boolean, 
  ): Unit = 
    rocksdb_block_based_options_set_cache_index_and_filter_blocks_with_high_priority(blockBasedOptions, arg1.toUByte()) 

  fun setPinL0FilterAndIndexBlocksInCache(
    arg1: Boolean, 
  ): Unit = 
    rocksdb_block_based_options_set_pin_l0_filter_and_index_blocks_in_cache(blockBasedOptions, arg1.toUByte()) 

  fun setPinTopLevelIndexAndFilter(
    arg1: Boolean, 
  ): Unit = 
    rocksdb_block_based_options_set_pin_top_level_index_and_filter(blockBasedOptions, arg1.toUByte()) 
}
