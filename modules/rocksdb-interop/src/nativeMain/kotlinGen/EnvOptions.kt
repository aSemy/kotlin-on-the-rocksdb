package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class EnvOptions(
  private val envOptions: CPointer<rocksdb_envoptions_t> = rocksdb_envoptions_create() ?: error("could not instantiate new EnvOptions")
) : CValuesRef<rocksdb_envoptions_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_envoptions_t> =
    envOptions.getPointer(scope)

  fun destroy(): Unit = 
    rocksdb_envoptions_destroy(envOptions) 
}
