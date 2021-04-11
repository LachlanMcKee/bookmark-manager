package net.lachlanmckee.bookmark.testing

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import net.lachlanmckee.bookmark.MainActivity
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MainActivityTest {
  @get:Rule
  val hiltRule = HiltAndroidRule(this)

  @get:Rule
  val activityRule = ActivityScenarioRule(MainActivity::class.java)

  @get:Rule
  val composeRule = createEmptyComposeRule()

  @Test
  fun verifyNavigation() {
    activityRule.scenario

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
