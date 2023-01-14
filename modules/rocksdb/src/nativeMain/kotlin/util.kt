package dev.adamko.kotlin.on.the.rocksdb

import kotlinx.cinterop.*


private const val UBYTE_FALSE: UByte = 0u
private const val INT_FALSE: Int = 0


/** Bools have the type `unsigned char` (`0 == false`; `rest == true`) */
internal inline fun UByte.toBoolean(): Boolean = this != UBYTE_FALSE

/** Bools have the type `unsigned char` (`0 == false`; `rest == true`) */
internal inline fun Boolean.toUByte(): UByte = if (this) 1u else UBYTE_FALSE

/** Bools have the type `unsigned char` (`0 == false`; `rest == true`) */
internal inline fun Boolean.toInt(): Int = if (this) 1 else INT_FALSE

///** Bools have the type `unsigned char` (`0 == false`; `rest == true`) */
//internal inline fun Boolean.convert(): Int = if (this) 1 else 0


 fun CPointer<ByteVar>.toKString(
  length: Int
): String = readBytes(length).toKString()
