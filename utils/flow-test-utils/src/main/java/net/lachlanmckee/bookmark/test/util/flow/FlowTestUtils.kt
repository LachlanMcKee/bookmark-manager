package net.lachlanmckee.bookmark.test.util.flow

import app.cash.turbine.ReceiveTurbine
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
fun suspendTest(startDispatcher: Boolean = true, body: suspend TestScope.() -> Unit) {
  val dispatcher: CoroutineDispatcher =
    if (startDispatcher) {
      UnconfinedTestDispatcher()
    } else {
      StandardTestDispatcher()
    }
  Dispatchers.setMain(dispatcher)
  runTest(dispatcher) {
    body()
  }
  Dispatchers.resetMain()
}

suspend fun <T> ReceiveTurbine<T>.assertItem(expectItem: T) {
  assertEquals(expectItem, awaitItem())
}
