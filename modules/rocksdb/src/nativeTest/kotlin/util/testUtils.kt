package dev.adamko.kotlin.on.the.rocksdb.util

import kotlin.random.Random
import kotlin.random.nextUInt
import kotlin.system.getTimeMillis

internal fun tempDir(): String {
  val dbName = "rocksdb_temp_${getTimeMillis()}${Random.nextUInt()}"
  return when (Platform.osFamily) {
    OsFamily.WINDOWS -> "C:\\Windows\\Temp\\$dbName"
    else             -> "/tmp/$dbName"
  }
}
