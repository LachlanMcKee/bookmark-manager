package net.lachlanmckee.bookmark.testing

import androidx.ui.test.createAndroidComposeRule
import dagger.hilt.android.testing.HiltAndroidTest
import net.lachlanmckee.bookmark.HiltTestActivity
import net.lachlanmckee.bookmark.feature.home.HomeFragment
import net.lachlanmckee.bookmark.testing.util.launchFragmentInHiltContainer
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class HomeFragmentTest {

  @get:Rule
  val composeTestRule =
    createAndroidComposeRule<HiltTestActivity>(disableTransitions = true)

  @Test
  fun givenValidLinkCopiedBeforeLaunch_whenLaunch_thenExpectCopyLink() {
    launchFragmentInHiltContainer<HomeFragment>()
  }
}
