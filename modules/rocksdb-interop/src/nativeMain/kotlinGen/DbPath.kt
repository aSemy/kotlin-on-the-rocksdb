package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class DbPath(
  private val dbPath: CPointer<rocksdb_dbpath_t>
) : CValuesRef<rocksdb_dbpath_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_dbpath_t> =
    dbPath.getPointer(scope)

  constructor(
    path: String?,
    targetSize: ULong,
  ): this(rocksdb_dbpath_create(path, targetSize) ?: error("could not instantiate new DbPath"))

  fun destroy(): Unit =
    rocksdb_dbpath_destroy(dbPath)
}
