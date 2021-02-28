package net.lachlanmckee.bookmark.test.util

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain

object LiveDataTester {
  fun testLiveData(testFunc: () -> Unit) {
    ArchTaskExecutor.getInstance()
      .setDelegate(object : TaskExecutor() {
        override fun executeOnDiskIO(runnable: Runnable) {
          runnable.run()
        }

        override fun postToMainThread(runnable: Runnable) {
          runnable.run()
        }

        override fun isMainThread(): Boolean {
          return true
        }
      })

    val testDispatcher = TestCoroutineDispatcher()
    Dispatchers.setMain(testDispatcher)

    testDispatcher.runBlockingTest { testFunc() }

    Dispatchers.resetMain()
    testDispatcher.cleanupTestCoroutines()

    ArchTaskExecutor.getInstance().setDelegate(null)
  }
}
