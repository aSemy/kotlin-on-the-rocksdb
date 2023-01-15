package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class SstFileWriter(
  private val sstFileWriter: CPointer<rocksdb_sstfilewriter_t>
) : CValuesRef<rocksdb_sstfilewriter_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_sstfilewriter_t> =
    sstFileWriter.getPointer(scope)

  constructor(
    env: CValuesRef<rocksdb_envoptions_t>?, 
    ioOptions: CValuesRef<rocksdb_options_t>?, 
  ): this(rocksdb_sstfilewriter_create(env, ioOptions) ?: error("could not instantiate new SstFileWriter"))
  
  constructor(
    env: CValuesRef<rocksdb_envoptions_t>?, 
    ioOptions: CValuesRef<rocksdb_options_t>?, 
    comparator: CValuesRef<rocksdb_comparator_t>?, 
  ): this(rocksdb_sstfilewriter_create_with_comparator(env, ioOptions, comparator) ?: error("could not instantiate new SstFileWriter"))

  fun open(
    name: String?, 
    errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
  ): Unit = 
    rocksdb_sstfilewriter_open(sstFileWriter, name, errorPointer) 

  fun add(
    key: String?, 
    keyLength: ULong, 
    value: String?, 
    valueLength: ULong, 
    errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
  ): Unit = 
    rocksdb_sstfilewriter_add(sstFileWriter, key, keyLength, value, valueLength, errorPointer) 

  fun put(
    key: String?, 
    keyLength: ULong, 
    value: String?, 
    valueLength: ULong, 
    errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
  ): Unit = 
    rocksdb_sstfilewriter_put(sstFileWriter, key, keyLength, value, valueLength, errorPointer) 

  fun putWithTs(
    key: String?, 
    keyLength: ULong, 
    ts: String?, 
    tsLength: ULong, 
    value: String?, 
    valueLength: ULong, 
    errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
  ): Unit = 
    rocksdb_sstfilewriter_put_with_ts(sstFileWriter, key, keyLength, ts, tsLength, value, valueLength, errorPointer) 

  fun merge(
    key: String?, 
    keyLength: ULong, 
    value: String?, 
    valueLength: ULong, 
    errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
  ): Unit = 
    rocksdb_sstfilewriter_merge(sstFileWriter, key, keyLength, value, valueLength, errorPointer) 

  fun delete(
    key: String?, 
    keyLength: ULong, 
    errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
  ): Unit = 
    rocksdb_sstfilewriter_delete(sstFileWriter, key, keyLength, errorPointer) 

  fun deleteWithTs(
    key: String?, 
    keyLength: ULong, 
    ts: String?, 
    tsLength: ULong, 
    errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
  ): Unit = 
    rocksdb_sstfilewriter_delete_with_ts(sstFileWriter, key, keyLength, ts, tsLength, errorPointer) 

  fun deleteRange(
    beginKey: String?, 
    beginKeyLength: ULong, 
    endKey: String?, 
    endKeyLength: ULong, 
    errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
  ): Unit = 
    rocksdb_sstfilewriter_delete_range(sstFileWriter, beginKey, beginKeyLength, endKey, endKeyLength, errorPointer) 

  fun finish(
    errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?, 
  ): Unit = 
    rocksdb_sstfilewriter_finish(sstFileWriter, errorPointer) 

  fun fileSize(
    fileSize: CValuesRef<ULongVarOf<ULong>>?, 
  ): Unit = 
    rocksdb_sstfilewriter_file_size(sstFileWriter, fileSize) 

  fun destroy(): Unit = 
    rocksdb_sstfilewriter_destroy(sstFileWriter) 
}
