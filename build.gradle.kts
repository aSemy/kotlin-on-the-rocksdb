import buildsrc.ext.excludeGeneratedGradleDsl

plugins {
  buildsrc.conventions.base
  idea
}

group = "dev.adamko.kotlin.rocksdb"
version = "0.0.1"

idea {
  module {
    excludeGeneratedGradleDsl(layout)
    excludeDirs.plusAssign(
      layout.files(
        ".idea",
        "gradle/wrapper",
      )
    )
  }
}
