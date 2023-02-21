package dev.adamko.kotlin.on.the.rocksdb

import org.rocksdb.*
import kotlinx.cinterop.*

class IngestExternalFileOptions(
  private val ingestExternalFileOptions: CPointer<rocksdb_ingestexternalfileoptions_t> = rocksdb_ingestexternalfileoptions_create() 
     ?: error("could not instantiate new IngestExternalFileOptions")
) : CValuesRef<rocksdb_ingestexternalfileoptions_t>() {

  override fun getPointer(scope: AutofreeScope): CPointer<rocksdb_ingestexternalfileoptions_t> =
    ingestExternalFileOptions.getPointer(scope)

  fun setMoveFiles(
    moveFiles: Boolean,
  ): Unit =
    rocksdb_ingestexternalfileoptions_set_move_files(ingestExternalFileOptions, moveFiles.toUByte())

  fun setSnapshotConsistency(
    snapshotConsistency: Boolean,
  ): Unit =
    rocksdb_ingestexternalfileoptions_set_snapshot_consistency(ingestExternalFileOptions, snapshotConsistency.toUByte())

  fun setAllowGlobalSeqno(
    allowGlobalSeqno: Boolean,
  ): Unit =
    rocksdb_ingestexternalfileoptions_set_allow_global_seqno(ingestExternalFileOptions, allowGlobalSeqno.toUByte())

  fun setAllowBlockingFlush(
    allowBlockingFlush: Boolean,
  ): Unit =
    rocksdb_ingestexternalfileoptions_set_allow_blocking_flush(ingestExternalFileOptions, allowBlockingFlush.toUByte())

  fun setIngestBehind(
    ingestBehind: Boolean,
  ): Unit =
    rocksdb_ingestexternalfileoptions_set_ingest_behind(ingestExternalFileOptions, ingestBehind.toUByte())

  fun destroy(): Unit =
    rocksdb_ingestexternalfileoptions_destroy(ingestExternalFileOptions)
}
