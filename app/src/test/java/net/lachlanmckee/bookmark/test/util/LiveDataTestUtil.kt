package net.lachlanmckee.bookmark.test.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Gets the value of a [LiveData] or waits for it to have one, with a timeout.
 *
 * Use this extension from host-side (JVM) tests. It's recommended to use it alongside
 * `InstantTaskExecutorRule` or a similar mechanism to execute tasks synchronously.
 */
fun <T> LiveData<T>.getOrAwaitValue(
  time: Long = 2,
  timeUnit: TimeUnit = TimeUnit.SECONDS,
  afterObserve: () -> Unit = {}
): T {
  var data: T? = null
  val latch = CountDownLatch(1)
  val observer = object : Observer<T> {
    override fun onChanged(o: T?) {
      data = o
      latch.countDown()
      this@getOrAwaitValue.removeObserver(this)
    }
  }
  this.observeForever(observer)

  afterObserve.invoke()

  // Don't wait indefinitely if the LiveData is not set.
  if (!latch.await(time, timeUnit)) {
    this.removeObserver(observer)
    throw TimeoutException("LiveData value was never set.")
  }

  @Suppress("UNCHECKED_CAST")
  return data as T
}

fun <T> LiveData<T>.getOrAwaitValues(
  numberOfValues: Int,
  executionFunc: (Int) -> Unit
): List<T> {
  var currentCount = 0
  val data: MutableList<T> = mutableListOf()
  val latch = CountDownLatch(numberOfValues)
  val observer = object : Observer<T> {
    override fun onChanged(o: T?) {
      data.add(o!!)
      latch.countDown()
      executionFunc.invoke(++currentCount)
      if (currentCount == numberOfValues) {
        this@getOrAwaitValues.removeObserver(this)
      }
    }
  }
  this.observeForever(observer)

  // Don't wait indefinitely if the LiveData is not set.
  if (!latch.await(2, TimeUnit.SECONDS)) {
    this.removeObserver(observer)
    throw TimeoutException("LiveData value was never set.")
  }

  return data
}
