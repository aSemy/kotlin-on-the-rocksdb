package dev.adamko.kotlin.on.the.rocksdb

import kotlin.annotation.AnnotationRetention.BINARY
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.FUNCTION

@RequiresOptIn(message = "Internal Kotlin on the RocksDb API. It may be changed in the future without notice.")
@Retention(BINARY)
@Target(CLASS, FUNCTION)
annotation class KotlinOnTheRocksDbInternalApi
