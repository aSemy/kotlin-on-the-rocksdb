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


val checkWorkflowRocksDBVersion by tasks.registering {
  group = LifecycleBasePlugin.VERIFICATION_GROUP

  val workflowDir = layout.projectDirectory.dir(".github/workflows/")
  inputs.dir(workflowDir).withPropertyName("workflowDir")

  val rdbVersion = libs.versions.rocksDb
  inputs.property("rdbVersion", rdbVersion)

  doLast {
    workflowDir.asFileTree.files.forEach { workflow ->
      val rocksDbVersionLines = workflow.readLines()
        .filter { !it.trim().startsWith("#") && it.contains("ROCKSDB_VERSION") }

      if (rocksDbVersionLines.isNotEmpty() && rocksDbVersionLines.none { line -> rdbVersion.get() in line }) {
        error("$workflow does not have latest RocksDB Version of ${rdbVersion.get()}")
      }
    }
  }
}

tasks.check {
  dependsOn(checkWorkflowRocksDBVersion)
}
