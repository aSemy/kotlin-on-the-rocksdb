Kotlin/Native interop with RocksDB.

Additional wrappers are generated on-demand:

```shell
./gradlew generateRocksDbWrappers
```

##### Misc notes

All functions can be extracted from `c.h` with regex

```regexp
rocksdb_\S+(?=\()
```


Maybe try and scrape docs from https://github.com/facebook/rocksdb/blob/main/include/rocksdb/options.h?
