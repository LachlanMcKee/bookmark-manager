package net.lachlanmckee.bookmark.util

import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

inline fun <T> T.runIf(condition: Boolean, block: T.() -> T): T {
  contract {
    callsInPlace(block, InvocationKind.EXACTLY_ONCE)
  }
  return if (condition) {
    block()
  } else {
    this
  }
}

inline fun <T, V> T.runIfNotNull(value: V?, block: T.(V) -> T): T {
  contract {
    callsInPlace(block, InvocationKind.EXACTLY_ONCE)
  }
  return if (value != null) {
    block(value)
  } else {
    this
  }
}
