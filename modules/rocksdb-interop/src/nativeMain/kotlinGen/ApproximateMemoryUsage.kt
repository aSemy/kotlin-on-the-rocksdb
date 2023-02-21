package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class ApproximateMemoryUsage(
  private val approximateMemoryUsage: CPointer<rocksdb_memory_usage_t>
) : CValuesRef<rocksdb_memory_usage_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_memory_usage_t> =
    approximateMemoryUsage.getPointer(scope)

  constructor(
    consumers: CValuesRef<rocksdb_memory_consumers_t>?,
    errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?,
  ): this(
    rocksdb_approximate_memory_usage_create(consumers, errorPointer) 
      ?: error("could not instantiate new ApproximateMemoryUsage")
  )

  fun destroy(): Unit =
    rocksdb_approximate_memory_usage_destroy(approximateMemoryUsage)

  val memTableTotal: ULong
    get() = rocksdb_approximate_memory_usage_get_mem_table_total(approximateMemoryUsage)

  val memTableUnflushed: ULong
    get() = rocksdb_approximate_memory_usage_get_mem_table_unflushed(approximateMemoryUsage)

  val memTableReadersTotal: ULong
    get() = rocksdb_approximate_memory_usage_get_mem_table_readers_total(approximateMemoryUsage)

  val cacheTotal: ULong
    get() = rocksdb_approximate_memory_usage_get_cache_total(approximateMemoryUsage)
}
