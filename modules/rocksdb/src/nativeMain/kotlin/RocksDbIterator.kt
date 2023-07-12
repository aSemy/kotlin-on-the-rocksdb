package dev.adamko.kotlin.on.the.rocksdb

import cnames.structs.rocksdb_iterator_t
import kotlinx.cinterop.*
import org.rocksdb.*
import platform.posix.size_tVar

class RocksDbIterator(
  private val iterator: CPointer<rocksdb_iterator_t>,
) : Iterator<Pair<String, String>> {

  fun seek(key: String): Unit = rocksdb_iter_seek(iterator, key, key.length.convert())
  fun seekForPrev(key: String): Unit = rocksdb_iter_seek_for_prev(iterator, key, key.length.convert())

  fun seekToFirst(): Unit = rocksdb_iter_seek_to_first(iterator)
  fun seekToLast(): Unit = rocksdb_iter_seek_to_last(iterator)

  override fun hasNext(): Boolean = rocksdb_iter_valid(iterator).toBoolean()

  fun destroy(): Unit = rocksdb_iter_destroy(iterator)

  override fun next(): Pair<String, String> {
    rocksdb_iter_next(iterator)
    return readKeyValue()
  }

  fun prev(): Pair<String, String> {
    rocksdb_iter_prev(iterator)
    return readKeyValue()
  }

  fun timestamp(): String = read(::rocksdb_iter_timestamp)

  fun hasError(): Boolean {
    memScoped {
      val err = allocPointerTo<ByteVar>()
      rocksdb_iter_get_error(iterator, err.ptr)
      return err.pointed?.value?.toBoolean() ?: false
    }
  }

  private fun readKeyValue(): Pair<String, String> =
    read(::rocksdb_iter_key) to read(::rocksdb_iter_value)

  private fun read(reader: IteratorValueReader): String {
    memScoped {
      val valueLength = alloc<size_tVar>()
      val valueBytes = reader(iterator, valueLength.ptr)
      return valueBytes?.toKString(valueLength.value.convert())
        ?: error("no 'next' value in iterator")
    }
  }
}


private typealias IteratorValueReader =
      (iterator: CValuesRef<rocksdb_iterator_t>?, valueLength: CValuesRef<size_tVar>?) -> CPointer<ByteVar>?
