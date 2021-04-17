package net.lachlanmckee.bookmark.test.util.flow

import app.cash.turbine.FlowTurbine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions.assertEquals

fun suspendTest(body: suspend TestCoroutineScope.() -> Unit) {
  val dispatcher = TestCoroutineDispatcher()
  Dispatchers.setMain(dispatcher)
  val scope = TestCoroutineScope()
  var throwable: Throwable? = null
  scope.launch {
    try {
      scope.body()
    } catch (t: Throwable) {
      throwable = t
    }
  }
  scope.cleanupTestCoroutines()
  Dispatchers.resetMain()

  throwable?.let { throw it }
}

suspend fun <T> FlowTurbine<T>.assertItem(expectItem: T) {
  assertEquals(expectItem, expectItem())
}
