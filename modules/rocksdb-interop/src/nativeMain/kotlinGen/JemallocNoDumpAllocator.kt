package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class JemallocNoDumpAllocator(
  private val jemallocNoDumpAllocator: CPointer<rocksdb_memory_allocator_t>
) : CValuesRef<rocksdb_memory_allocator_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_memory_allocator_t> =
    jemallocNoDumpAllocator.getPointer(scope)

  constructor(
    errorPointer: CValuesRef<CPointerVarOf<CPointer<ByteVar>>>?,
  ): this(rocksdb_jemalloc_nodump_allocator_create(errorPointer) ?: error("could not instantiate new JemallocNoDumpAllocator"))
}
