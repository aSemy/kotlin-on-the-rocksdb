package dev.adamko.kotlin.on.the.rocksdb

import kotlin.test.Test
import kotlin.test.assertEquals
import org.rocksdb.AccessHint

class AccessHintTest {

  @Test
  fun `validate AccessHint integer values`() {
    assertEquals(0u, AccessHint.NONE.value)
    assertEquals(1u, AccessHint.NORMAL.value)
    assertEquals(2u, AccessHint.SEQUENTIAL.value)
    assertEquals(3u, AccessHint.WILL_NEED.value)
  }

}
