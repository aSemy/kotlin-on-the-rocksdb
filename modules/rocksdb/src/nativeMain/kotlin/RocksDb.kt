package dev.adamko.kotlin.on.the.rocksdb

import cnames.structs.rocksdb_t
import kotlinx.cinterop.*
import org.rocksdb.*
import platform.posix.size_tVar


/**
 * A RocksDB key-value store.
 */
class RocksDb(
  directory: String,
  dbOptions: RocksDbOptions,
  private val readOptions: RocksDbReadOptions,
  private val writeOptions: RocksDbWriteOptions,
  private val db: CPointer<rocksdb_t> = create(dbOptions, directory),
) : CValuesRef<rocksdb_t>() {

  operator fun get(key: String): String? {
    memScoped {
      val err = allocPointerTo<ByteVar>()
      val valueLength = alloc<size_tVar>()
      val valueBytes = rocksdb_get(
        db = db,
        options = readOptions,
        key = key,
        keylen = key.length.convert(),
        vallen = valueLength.ptr,
        errptr = err.ptr
      )
      val errResult = err.pointed?.value
      require(errResult == null) { "error reading value for key $key: $errResult" }

      return valueBytes?.toKString(valueLength.value.convert())
    }
  }

  operator fun set(key: String, value: String) {
    return memScoped {
      val err = allocPointerTo<ByteVar>()
      rocksdb_put(
        db = db,
        options = writeOptions,
        key = key,
        keylen = key.length.convert(),
        `val` = value,
        vallen = value.length.convert(),
        errptr = err.ptr
      )
      val result = err.pointed?.value
      require(result == null) { "error setting value for key $key: $result" }
    }
  }

  fun iterator(readOptions: RocksDbReadOptions = this@RocksDb.readOptions): RocksDbIterator =
    RocksDbIterator(rocksdb_create_iterator(db, readOptions) ?: error("could not create iterator"))

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_t> = db.getPointer(scope)

  fun close() = rocksdb_close(db)

  companion object {
    fun create(
      options: RocksDbOptions,
      directory: String,
    ): CPointer<rocksdb_t> {
      memScoped {

        val err = allocPointerTo<ByteVar>()
        val db = rocksdb_open(options, directory, err.ptr) ?: error("could not open RocksDb")

        val result = err.pointed?.value
        require(result == null) { "error opening RocksDb: $result" }

        return db
      }
    }
  }
}
