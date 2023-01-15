# [Kotlin on the RocksDB](https://github.com/aSemy/kotlin-on-the-rocksdb)

[Kotlin/Native](https://kotlinlang.org/docs/native-overview.html) bindings for [RocksDB](https://github.com/facebook/rocksdb/).

#### Status

Very early prototype.

Bindings are generated from the
[RocksDB C API](https://github.com/facebook/rocksdb/blob/main/include/rocksdb/c.h),
which has some limitations.

Using the C bindings potentially allows for targeting Windows, Linux, and MacOS.

#### Building

Building requires that the following libraries are installed.

* `rocksdb`
* `bz2`
* `lz4`
* `snappy`
* `zlib`
* `zstd`
