package net.lachlanmckee.bookmark.testing

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication
import net.lachlanmckee.bookmark.testing.util.annotation.ApplitoolsTest
import timber.log.Timber

class CustomTestRunner : AndroidJUnitRunner() {
  override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
    Timber.plant(Timber.DebugTree())
    return super.newApplication(cl, HiltTestApplication::class.java.name, context)
  }

  override fun onCreate(arguments: Bundle?) {
    arguments?.putString("filter", ApplitoolsTest.ApplitoolsFilter::class.java.name)
    super.onCreate(arguments)
  }
}
