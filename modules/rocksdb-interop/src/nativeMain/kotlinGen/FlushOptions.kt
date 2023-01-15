package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class FlushOptions(
  private val flushOptions: CPointer<rocksdb_flushoptions_t> = rocksdb_flushoptions_create() ?: error("could not instantiate new FlushOptions")
) : CValuesRef<rocksdb_flushoptions_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_flushoptions_t> =
    flushOptions.getPointer(scope)

  fun destroy(): Unit = 
    rocksdb_flushoptions_destroy(flushOptions) 

  var wait: Boolean
    get() = rocksdb_flushoptions_get_wait(flushOptions).toBoolean()
    set(value) = rocksdb_flushoptions_set_wait(flushOptions, value.toUByte())
}
