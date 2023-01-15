package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class FifoCompactionOptions(
  private val fifoCompactionOptions: CPointer<rocksdb_fifo_compaction_options_t> = rocksdb_fifo_compaction_options_create() ?: error("could not instantiate new FifoCompactionOptions")
) : CValuesRef<rocksdb_fifo_compaction_options_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_fifo_compaction_options_t> =
    fifoCompactionOptions.getPointer(scope)

  fun destroy(): Unit = 
    rocksdb_fifo_compaction_options_destroy(fifoCompactionOptions) 

  var maxTableFilesSize: ULong
    get() = rocksdb_fifo_compaction_options_get_max_table_files_size(fifoCompactionOptions)
    set(value) = rocksdb_fifo_compaction_options_set_max_table_files_size(fifoCompactionOptions, value)
}
