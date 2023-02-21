package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class MemoryConsumers(
  private val memoryConsumers: CPointer<rocksdb_memory_consumers_t> = rocksdb_memory_consumers_create() 
     ?: error("could not instantiate new MemoryConsumers")
) : CValuesRef<rocksdb_memory_consumers_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_memory_consumers_t> =
    memoryConsumers.getPointer(scope)

  fun addDb(
    db: CValuesRef<rocksdb_t>?,
  ): Unit =
    rocksdb_memory_consumers_add_db(memoryConsumers, db)

  fun addCache(
    cache: CValuesRef<rocksdb_cache_t>?,
  ): Unit =
    rocksdb_memory_consumers_add_cache(memoryConsumers, cache)

  fun destroy(): Unit =
    rocksdb_memory_consumers_destroy(memoryConsumers)
}
