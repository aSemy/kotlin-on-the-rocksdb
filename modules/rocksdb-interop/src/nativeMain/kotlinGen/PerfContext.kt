package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class PerfContext(
  private val perfContext: CPointer<rocksdb_perfcontext_t> = rocksdb_perfcontext_create() ?: error("could not instantiate new PerfContext")
) : CValuesRef<rocksdb_perfcontext_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_perfcontext_t> =
    perfContext.getPointer(scope)

  fun reset(): Unit =
    rocksdb_perfcontext_reset(perfContext)

  fun report(
    excludeZeroCounters: Boolean,
  ): CPointer<ByteVar>? =
    rocksdb_perfcontext_report(perfContext, excludeZeroCounters.toUByte())

  fun metric(
    metric: Int,
  ): ULong =
    rocksdb_perfcontext_metric(perfContext, metric)

  fun destroy(): Unit =
    rocksdb_perfcontext_destroy(perfContext)
}
