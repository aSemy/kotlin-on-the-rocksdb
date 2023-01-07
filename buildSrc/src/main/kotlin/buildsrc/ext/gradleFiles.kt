package buildsrc.ext

import org.gradle.api.file.RelativePath


/** Drop the first [count] directories from the path */
fun RelativePath.dropDirectories(count: Int): RelativePath =
  RelativePath(true, *segments.drop(count).toTypedArray())


/** Drop the first directory from the path */
fun RelativePath.dropDirectory(): RelativePath =
  dropDirectories(1)


/** Drop the first directory from the path */
fun RelativePath.dropDirectoriesWhile(
  segmentPrediate: (segment: String) -> Boolean
): RelativePath =
  RelativePath(
    true,
    *segments.dropWhile(segmentPrediate).toTypedArray(),
  )
