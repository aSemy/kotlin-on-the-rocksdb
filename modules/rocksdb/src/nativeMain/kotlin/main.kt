package dev.adamko.kotlin.on.the.rocksdb

import kotlinx.cinterop.memScoped
import kotlinx.coroutines.*
import org.rocksdb.rocksdb_close

fun main(args: Array<String>): Unit = runBlocking {
  val dbDir = args.firstOrNull() ?: when (Platform.osFamily) {
    OsFamily.WINDOWS -> "C:\\Windows\\TEMP\\rocksdb_temp_01"
    else             -> "/tmp/rocksdb_temp_01"
  }

  println("running rocksdb in dir $dbDir")

  memScoped {
    val options = RocksDbOptions {
      increaseParallelism(Platform.getAvailableProcessors())
      optimizeLevelStyleCompaction(0u)
      createIfMissing = true
    }

    val writeOptions = RocksDbWriteOptions()
    val readOptions = RocksDbReadOptions()
    val db = RocksDb(
      directory = dbDir,
      dbOptions = options,
      readOptions = readOptions,
      writeOptions = writeOptions,
    )

    coroutineScope {
      println("launched Coroutine scope")

      launch {
        repeat(50) {
          db["test-key-$it"] = "cool value $it ${kotlin.system.getTimeMillis()}"
          println("emitted $it")
          delay(500)
        }
        cancel("finished emitting")
      }


      val iterator = db.iterator()

      launch {

        var i = 0

        iterator.seekToFirst()

        while (isActive) {
          while (iterator.hasNext()) {
            val v = iterator.next()
            println("[${i++}] got next value: $v")
//            delay(250)
          }
          yield()
        }
        println("finished iterator")

//        repeat(50) {
//          val value = db["test-key"]
//          println("[$it] got value from DB: $value")
//          delay(500)
//        }
      }
    }

    rocksdb_close(db)
  }
}
