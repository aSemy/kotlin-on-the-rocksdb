package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class LruCacheOptions(
  private val lruCacheOptions: CPointer<rocksdb_lru_cache_options_t> = rocksdb_lru_cache_options_create() 
     ?: error("could not instantiate new LruCacheOptions")
) : CValuesRef<rocksdb_lru_cache_options_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_lru_cache_options_t> =
    lruCacheOptions.getPointer(scope)

  fun destroy(): Unit =
    rocksdb_lru_cache_options_destroy(lruCacheOptions)

  fun setCapacity(
    arg1: ULong,
  ): Unit =
    rocksdb_lru_cache_options_set_capacity(lruCacheOptions, arg1)

  fun setNumShardBits(
    arg1: Int,
  ): Unit =
    rocksdb_lru_cache_options_set_num_shard_bits(lruCacheOptions, arg1)

  fun setMemoryAllocator(
    arg1: CValuesRef<rocksdb_memory_allocator_t>?,
  ): Unit =
    rocksdb_lru_cache_options_set_memory_allocator(lruCacheOptions, arg1)
}
