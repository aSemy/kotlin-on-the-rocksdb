# Kotlin on the Rocks

Kotlin/Native bindings for [RocksDB](https://github.com/facebook/rocksdb/).

#### Status

Very early prototype.

Bindings are generated from the
[RocksDB C API](https://github.com/facebook/rocksdb/blob/main/include/rocksdb/c.h),
which has some limitations.

Using the C bindings potentially allows for targeting Windows, Linux, and MacOS.

#### Building

Building requires that the following libraries are installed.

* `rocksdb`
* `lz4`
* `snappy`
* `zstd`
* `bz2`
* `snappy`
* `zlib`
