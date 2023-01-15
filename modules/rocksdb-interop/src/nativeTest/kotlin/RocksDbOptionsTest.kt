package dev.adamko.kotlin.on.the.rocksdb

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import org.rocksdb.AccessHint

class RocksDbOptionsTest {

//  @Test
//  fun `can create and copy`() {
//    val options = RocksDbOptions()
//    assertNotNull(options)
//
//    val copy = options.createCopy()
//    assertNotNull(copy)
//  }
//
//  @Test
//  fun `can set access hints`() {
//    val options = RocksDbOptions()
//
//    AccessHint.values().forEach { hint ->
//      options.accessHintOnCompactionStart = hint
//      assertEquals(hint, options.accessHintOnCompactionStart)
//    }
//  }
//
//  @Test
//  fun `vars can be written and read`() {
//    val options = RocksDbOptions()
//
//    assertNotNull(options.accessHintOnCompactionStart)
//    assertNotNull(options.adviseRandomOnOpen)
//    assertNotNull(options.allowConcurrentMemtableWrite)
//    assertNotNull(options.allowIngestBehind)
//    assertNotNull(options.allowMmapReads)
//    assertNotNull(options.allowMmapWrites)
//    assertNotNull(options.arenaBlockSize)
//    assertNotNull(options.atomicFlush)
//    assertNotNull(options.avoidUnnecessaryBlockingIo)
//    assertNotNull(options.blobCompactionReadAheadSize)
//    assertNotNull(options.blobCompressionType)
//    assertNotNull(options.blobFileSize)
//    assertNotNull(options.blobFileStartingLevel)
//    assertNotNull(options.blobGcAgeCutoff)
//    assertNotNull(options.blobGcForceThreshold)
//    assertNotNull(options.bloomLocality)
//    assertNotNull(options.bottommostCompression)
//    assertNotNull(options.bytesPerSync)
//    assertNotNull(options.compactionStyle)
//    assertNotNull(options.compression)
//    assertNotNull(options.compressionOptionsMaxDictBufferBytes)
//    assertNotNull(options.compressionOptionsParallelThreads)
//    assertNotNull(options.compressionOptionsUseZstdDictTrainer)
//    assertNotNull(options.compressionOptionsZstdMaxTrainBytes)
//    assertNotNull(options.createIfMissing)
//    assertNotNull(options.createMissingColumnFamilies)
//    assertNotNull(options.dbWriteBufferSize)
//    assertNotNull(options.deleteObsoleteFilesPeriodMicros)
//    assertNotNull(options.disableAutoCompactions)
//    assertNotNull(options.enableBlobFiles)
//    assertNotNull(options.enableBlobGc)
//    assertNotNull(options.enablePipelinedWrite)
//    assertNotNull(options.enableWriteThreadAdaptiveYield)
//    assertNotNull(options.errorIfExists)
//    assertNotNull(options.experimentalMemPurgeThreshold)
//    assertNotNull(options.hardPendingCompactionBytesLimit)
//    assertNotNull(options.infoLogLevel)
//    assertNotNull(options.inplaceUpdateNumLocks)
//    assertNotNull(options.inplaceUpdateSupport)
//    assertNotNull(options.isFdCloseOnExec)
//    assertNotNull(options.keepLogFileNum)
//    assertNotNull(options.level0FileNumCompactionTrigger)
//    assertNotNull(options.level0SlowdownWritesTrigger)
//    assertNotNull(options.level0StopWritesTrigger)
//    assertNotNull(options.levelCompactionDynamicLevelBytes)
//    assertNotNull(options.logFileTimeToRoll)
//    assertNotNull(options.manifestPreallocationSize)
//    assertNotNull(options.manualWalFlush)
//    assertNotNull(options.maxBackgroundCompactions)
//    assertNotNull(options.maxBackgroundFlushes)
//    assertNotNull(options.maxBackgroundJobs)
//    assertNotNull(options.maxBytesForLevelBase)
//    assertNotNull(options.maxBytesForLevelMultiplier)
//    assertNotNull(options.maxCompactionBytes)
//    assertNotNull(options.maxFileOpeningThreads)
//    assertNotNull(options.maxLogFileSize)
//    assertNotNull(options.maxManifestFileSize)
//    assertNotNull(options.maxOpenFiles)
//    assertNotNull(options.maxSequentialSkipInIterations)
//    assertNotNull(options.maxSubCompactions)
//    assertNotNull(options.maxSuccessiveMerges)
//    assertNotNull(options.maxTotalWalSize)
//    assertNotNull(options.maxWriteBufferNumber)
//    assertNotNull(options.maxWriteBufferNumberToMaintain)
//    assertNotNull(options.maxWriteBufferSizeToMaintain)
//    assertNotNull(options.memtableHugePageSize)
//    assertNotNull(options.memtablePrefixBloomSizeRatio)
//    assertNotNull(options.minBlobSize)
//    assertNotNull(options.minWriteBufferNumberToMerge)
//    assertNotNull(options.numLevels)
//    assertNotNull(options.optimizeFiltersForHits)
//    assertNotNull(options.paranoidChecks)
//    assertNotNull(options.prepopulateBlobCache)
//    assertNotNull(options.recycleLogFileNum)
//    assertNotNull(options.reportBgIoStats)
//    assertNotNull(options.skipCheckingSstFileSizesOnDbOpen)
//    assertNotNull(options.skipStatsUpdateOnDbOpen)
//    assertNotNull(options.softPendingCompactionBytesLimit)
//    assertNotNull(options.statsDumpPeriodSec)
//    assertNotNull(options.statsPersistPeriodSec)
//    assertNotNull(options.tableCacheNumshardbits)
//    assertNotNull(options.targetFileSizeBase)
//    assertNotNull(options.targetFileSizeMultiplier)
//    assertNotNull(options.unorderedWrite)
//    assertNotNull(options.useAdaptiveMutex)
//    assertNotNull(options.useDirectIoForFlushAndCompaction)
//    assertNotNull(options.useDirectReads)
//    assertNotNull(options.useFsync)
//    assertNotNull(options.walBytesPerSync)
//    assertNotNull(options.walCompression)
//    assertNotNull(options.walRecoveryMode)
//    assertNotNull(options.walSizeLimitMB)
//    assertNotNull(options.walTtlSeconds)
//    assertNotNull(options.writableFileMaxBufferSize)
//    assertNotNull(options.writeBufferSize)
//  }
}
