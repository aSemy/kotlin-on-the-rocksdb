import dev.adamko.kotlin.on.the.rocksdb.RocksDb
import dev.adamko.kotlin.on.the.rocksdb.RocksDbOptions
import dev.adamko.kotlin.on.the.rocksdb.RocksDbReadOptions
import dev.adamko.kotlin.on.the.rocksdb.RocksDbWriteOptions
import kotlin.system.getTimeMillis
import kotlin.test.Ignore
import kotlin.test.Test
import kotlinx.coroutines.*

class RocksDbTest {

  @Test
  fun noopTest() = runBlocking {
    println("noop")
  }

  @Test
  fun openCloseDbTest() = runBlocking {

    val tempDir = when (Platform.osFamily) {
      OsFamily.WINDOWS -> "C:\\Windows\\Temp\\rocksdb_temp_${getTimeMillis()}"
      else             -> "/tmp/rocksdb_temp_${getTimeMillis()}"
    }

    println("testing rdb with file $tempDir")

    val options = RocksDbOptions {
//      increaseParallelism(Platform.getAvailableProcessors())
      optimizeLevelStyleCompaction(0u)
      createIfMissing = true
    }

    val writeOptions = RocksDbWriteOptions()
    val readOptions = RocksDbReadOptions()
    val db = RocksDb(
      directory = tempDir,
      dbOptions = options,
      readOptions = readOptions,
      writeOptions = writeOptions,
    )

    db.close()
  }

  @Test
  @Ignore
  fun test() = runBlocking {

    val tempDir = when (Platform.osFamily) {
      OsFamily.WINDOWS -> "C:\\Windows\\Temp\\rocksdb_temp_${getTimeMillis()}"
      else             -> "/tmp/rocksdb_temp_${getTimeMillis()}"
    }

    println("testing rdb with file $tempDir")

    val options = RocksDbOptions {
//      increaseParallelism(Platform.getAvailableProcessors())
      optimizeLevelStyleCompaction(0u)
      createIfMissing = true
    }

    val writeOptions = RocksDbWriteOptions()
    val readOptions = RocksDbReadOptions()
    val db = RocksDb(
      directory = tempDir,
      dbOptions = options,
      readOptions = readOptions,
      writeOptions = writeOptions,
    )

    coroutineScope {
      println("launched Coroutine scope")

      launch {
        repeat(50) {
          db["test-key-$it"] = "cool value $it ${getTimeMillis()}"
          println("emitted $it")
          delay(500)
        }
        cancel("finished emitting")
      }


      val iterator = db.iterator()

      launch {

        var i = 0

        iterator.seekToFirst()

//        while (isActive) {
        while (iterator.hasNext()) {
          val v = iterator.next()
          println("[${i++}] got next value: $v")
          delay(250)
        }
//          yield()
//        }
        println("finished iterator")

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
