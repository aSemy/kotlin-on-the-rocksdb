package buildsrc.ext

import org.gradle.api.artifacts.Configuration


/** Mark this [Configuration] as one that will be consumed by other subprojects. */
fun Configuration.asProvider(visible: Boolean = false) {
  isVisible = visible
  isCanBeResolved = false
  isCanBeConsumed = true
}

/** Mark this [Configuration] as one that will consume artifacts from other subprojects (also known as 'resolving') */
fun Configuration.asConsumer(visible: Boolean = false) {
  isVisible = visible
  isCanBeResolved = true
  isCanBeConsumed = false
}
