package net.lachlanmckee.bookmark.testing

import androidx.compose.foundation.layout.ExperimentalLayout
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltAndroidTest
import net.lachlanmckee.bookmark.HiltTestActivity
import net.lachlanmckee.bookmark.feature.home.HomeFragment
import net.lachlanmckee.bookmark.testing.util.launchFragmentInHiltContainer
import org.junit.Rule
import org.junit.Test

@ExperimentalLayout
@HiltAndroidTest
class HomeFragmentTest {

  @get:Rule
  val composeTestRule =
    createAndroidComposeRule<HiltTestActivity>()

  @Test
  fun givenValidLinkCopiedBeforeLaunch_whenLaunch_thenExpectCopyLink() {
    launchFragmentInHiltContainer<HomeFragment>()
  }
}
