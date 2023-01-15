package dev.adamko.kotlin.on.the.rocksdb

import kotlinx.cinterop.*


@PublishedApi
internal const val UBYTE_FALSE: UByte = 0u
@PublishedApi
internal const val INT_FALSE: Int = 0


/** Bools have the type `unsigned char` (`0 == false`; `rest == true`) */
@KotlinOnTheRocksDbInternalApi
inline fun UByte.toBoolean(): Boolean = this != UBYTE_FALSE

/** Bools have the type `unsigned char` (`0 == false`; `rest == true`) */
@KotlinOnTheRocksDbInternalApi
inline fun Boolean.toUByte(): UByte = if (this) 1u else UBYTE_FALSE

/** Bools have the type `unsigned char` (`0 == false`; `rest == true`) */
@KotlinOnTheRocksDbInternalApi
inline fun Boolean.toInt(): Int = if (this) 1 else INT_FALSE

///** Bools have the type `unsigned char` (`0 == false`; `rest == true`) */
//internal inline fun Boolean.convert(): Int = if (this) 1 else 0


fun CPointer<ByteVar>.toKString(
  length: Int
): String = readBytes(length).toKString()


@RequiresOptIn(message = "Internal Kotlin on the RocksDb API. It may be changed in the future without notice.")
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class KotlinOnTheRocksDbInternalApi
