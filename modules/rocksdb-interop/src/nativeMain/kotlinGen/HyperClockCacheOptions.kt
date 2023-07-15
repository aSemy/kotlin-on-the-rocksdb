package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class HyperClockCacheOptions(
  private val hyperClockCacheOptions: CPointer<rocksdb_hyper_clock_cache_options_t>
) : CValuesRef<rocksdb_hyper_clock_cache_options_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_hyper_clock_cache_options_t> =
    hyperClockCacheOptions.getPointer(scope)

  constructor(
    capacity: ULong,
    estimatedEntryCharge: ULong,
  ): this(
    rocksdb_hyper_clock_cache_options_create(capacity, estimatedEntryCharge) 
      ?: error("could not instantiate new HyperClockCacheOptions")
  )

  fun destroy(): Unit =
    rocksdb_hyper_clock_cache_options_destroy(hyperClockCacheOptions)

  fun setCapacity(
    arg1: ULong,
  ): Unit =
    rocksdb_hyper_clock_cache_options_set_capacity(hyperClockCacheOptions, arg1)

  fun setEstimatedEntryCharge(
    arg1: ULong,
  ): Unit =
    rocksdb_hyper_clock_cache_options_set_estimated_entry_charge(hyperClockCacheOptions, arg1)

  fun setNumShardBits(
    arg1: Int,
  ): Unit =
    rocksdb_hyper_clock_cache_options_set_num_shard_bits(hyperClockCacheOptions, arg1)

  fun setMemoryAllocator(
    arg1: CValuesRef<rocksdb_memory_allocator_t>?,
  ): Unit =
    rocksdb_hyper_clock_cache_options_set_memory_allocator(hyperClockCacheOptions, arg1)
}
