package net.lachlanmckee.bookmark.testing.home

import androidx.compose.ui.test.junit4.createEmptyComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import net.lachlanmckee.bookmark.feature.home.HomeFragment
import net.lachlanmckee.bookmark.testing.util.hilt.launchFragmentInHiltContainer
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class HomeFragmentTest {

  @get:Rule
  val composeTestRule = createEmptyComposeRule()

  @get:Rule
  val hiltRule = HiltAndroidRule(this)

  @Test
  fun givenValidLinkCopiedBeforeLaunch_whenLaunch_thenExpectCopyLink() {
    launchFragmentInHiltContainer<HomeFragment>()
  }
}
