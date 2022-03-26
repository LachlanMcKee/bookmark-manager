package net.lachlanmckee.bookmark.testing

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import net.lachlanmckee.bookmark.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

@HiltAndroidTest
class MainActivityTest {
  private val composeRule = createAndroidComposeRule<MainActivity>()

  @get:Rule
  val chain: RuleChain = RuleChain
    .outerRule(HiltAndroidRule(this))
    .around(composeRule)

  @Test
  fun verifyNavigation() {
    with(composeRule) {
      // Home
      onNodeWithText("Bookmarks").assertIsDisplayed()
      onNodeWithTag("Search navigation").performClick()

      // Search
      onNodeWithText("Bookmark Search").assertIsDisplayed()
      onNodeWithTag("SearchText").performImeAction() // Close the keyboard
      onNodeWithTag("Settings navigation").performClick()

      // Settings
      onNodeWithText("Settings").assertIsDisplayed()
      onNodeWithTag("Home navigation").performClick()

      // Home
      onNodeWithText("Bookmarks").assertIsDisplayed()
    }
  }
}
