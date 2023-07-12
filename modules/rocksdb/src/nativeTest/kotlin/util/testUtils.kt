package dev.adamko.kotlin.on.the.rocksdb.util

import kotlinx.cinterop.*
import platform.posix.CLOCK_REALTIME
import platform.posix.clock_gettime
import platform.posix.timespec
import kotlin.random.Random
import kotlin.random.nextUInt
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds

internal fun tempDir(): String {
  val dbName = "rocksdb_temp_${systemTimeMillis()}_${Random.nextUInt()}"
  return when (Platform.osFamily) {
    OsFamily.WINDOWS -> "C:\\Windows\\Temp\\$dbName"
    else             -> "/tmp/$dbName/"
  }
}

private fun systemTimeMillis(): Long {
  return memScoped {
    val timespec = alloc<timespec>()
    clock_gettime(CLOCK_REALTIME.convert(), timespec.ptr)
    @OptIn(UnsafeNumber::class)
    timespec
      .run { tv_sec.seconds + tv_nsec.convert<Long>().nanoseconds }
      .inWholeMilliseconds
  }
}
