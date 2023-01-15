package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class CuckooOptions(
  private val cuckooOptions: CPointer<rocksdb_cuckoo_table_options_t> = rocksdb_cuckoo_options_create() ?: error("could not instantiate new CuckooOptions")
) : CValuesRef<rocksdb_cuckoo_table_options_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_cuckoo_table_options_t> =
    cuckooOptions.getPointer(scope)

  fun destroy(): Unit = 
    rocksdb_cuckoo_options_destroy(cuckooOptions) 

  fun setHashRatio(
    v: Double, 
  ): Unit = 
    rocksdb_cuckoo_options_set_hash_ratio(cuckooOptions, v) 

  fun setMaxSearchDepth(
    v: UInt, 
  ): Unit = 
    rocksdb_cuckoo_options_set_max_search_depth(cuckooOptions, v) 

  fun setCuckooBlockSize(
    v: UInt, 
  ): Unit = 
    rocksdb_cuckoo_options_set_cuckoo_block_size(cuckooOptions, v) 

  fun setIdentityAsFirstHash(
    v: Boolean, 
  ): Unit = 
    rocksdb_cuckoo_options_set_identity_as_first_hash(cuckooOptions, v.toUByte()) 

  fun setUseModuleHash(
    v: Boolean, 
  ): Unit = 
    rocksdb_cuckoo_options_set_use_module_hash(cuckooOptions, v.toUByte()) 
}
