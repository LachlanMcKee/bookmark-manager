package net.lachlanmckee.bookmark.testing

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication
import timber.log.Timber

class CustomTestRunner : AndroidJUnitRunner() {
  override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
    Timber.plant(Timber.DebugTree())
    return super.newApplication(cl, HiltTestApplication::class.java.name, context)
  }
}
