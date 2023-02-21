package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class RateLimiter(
  private val rateLimiter: CPointer<rocksdb_ratelimiter_t>
) : CValuesRef<rocksdb_ratelimiter_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_ratelimiter_t> =
    rateLimiter.getPointer(scope)

  constructor(
    rateBytesPerSec: Long,
    refillPeriodUs: Long,
    fairness: Int,
  ): this(
    rocksdb_ratelimiter_create(rateBytesPerSec, refillPeriodUs, fairness) 
      ?: error("could not instantiate new RateLimiter")
  )

  fun destroy(): Unit =
    rocksdb_ratelimiter_destroy(rateLimiter)
}
