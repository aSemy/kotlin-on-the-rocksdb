package dev.adamko.kotlin.on.the.rocksdb

import dev.adamko.kotlin.on.the.rocksdb.util.tempDir
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestResult
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds

class RocksDbTest {

  @Test
  fun openCloseDbTest(): TestResult = runTest {

    val tempDir = tempDir()

    val options = RocksDbOptions().apply {
//      increaseParallelism(Platform.getAvailableProcessors())
      optimizeLevelStyleCompaction(0u)
      createIfMissing = true
    }

    val writeOptions = WriteOptions()
    val readOptions = ReadOptions()
    val db = RocksDb(
      directory = tempDir,
      dbOptions = options,
      readOptions = readOptions,
      writeOptions = writeOptions,
    )

    db.close()
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun test(): TestResult = runTest {
    val tempDir = tempDir()

    println("testing rdb with file $tempDir")

    val options = RocksDbOptions().apply {
//      increaseParallelism(Platform.getAvailableProcessors())
      optimizeLevelStyleCompaction(0u)
      createIfMissing = true
    }

    val writeOptions = WriteOptions()
    val readOptions = ReadOptions()
    val db = RocksDb(
      directory = tempDir,
      dbOptions = options,
      readOptions = readOptions,
      writeOptions = writeOptions,
    )

    coroutineScope {
      println("launched Coroutine scope")
      db["first value"] = "an initial value"

      launch {
        repeat(50) { i ->
          val key = "test-key-$i"
          val value = "cool value $i @time:${currentTime}"
          db[key] = value
          assertEquals(
            message = "expect correct value in DB for i:$i @time:${currentTime}",
            expected = value,
            actual = db[key],
          )
          println("emit[$i] k:$key, value:$value @time:${currentTime}")
          delay(500.milliseconds)
        }
        println("finished emitting @time:$currentTime")
      }
      delay(1500.milliseconds)

      val iterator = db.iterator()

      launch {
        var readValues = 0

        iterator.seekToFirst()

        while (iterator.hasNext()) {
          val v = iterator.next()
          println("read[${++readValues}] got next value of '$v' @time:${currentTime}")
          delay(250.milliseconds)
        }

        println("finished iterator @time:${currentTime}")

        assertEquals(
          message = "expect iterator reads 4 values",
          expected = 4,
          actual = readValues,
        )
//        repeat(50) {
//          val value = db["test-key"]
//          println("[$it] got value from DB: $value")
//          delay(500)
//        }
      }
    }

    db.close()
  }

}
