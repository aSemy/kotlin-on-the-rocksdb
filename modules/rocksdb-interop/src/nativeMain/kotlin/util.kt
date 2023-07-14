package dev.adamko.kotlin.on.the.rocksdb

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.toKString


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


/** Create a Kotlin String from the first [length] bytes of ths `CPointer<ByteVar>` */
@KotlinOnTheRocksDbInternalApi
inline fun CPointer<ByteVar>.toKString(length: Int): String =
  readBytes(length).toKString()
