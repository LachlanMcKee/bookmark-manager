package net.lachlanmckee.bookmark.feature.home

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

@ExperimentalCoroutinesApi
class ImmediateCoroutineExtension : BeforeEachCallback, AfterEachCallback {
  private val testDispatcher = TestCoroutineDispatcher()

  override fun beforeEach(context: ExtensionContext?) {
    Dispatchers.setMain(testDispatcher)
  }

  override fun afterEach(context: ExtensionContext?) {
    Dispatchers.resetMain()
    testDispatcher.cleanupTestCoroutines()
  }

} 