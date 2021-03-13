package net.lachlanmckee.bookmark.testing.applitools

import com.applitools.eyes.android.espresso.Eyes

class EyesWrapperImpl(private val eyes: Eyes) : EyesWrapper {
  override fun checkWindow(tag: String) {
    eyes.checkWindow(tag)
  }
}
