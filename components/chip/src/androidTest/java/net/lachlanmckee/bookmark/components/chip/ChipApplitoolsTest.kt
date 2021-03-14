package net.lachlanmckee.bookmark.components.chip

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.AnnotatedString
import net.lachlanmckee.bookmark.testing.applitools.ApplitoolsTest
import net.lachlanmckee.bookmark.testing.applitools.eyesTest
import org.junit.Rule
import org.junit.Test

@ApplitoolsTest
class HomeScreenApplitoolsTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun verifyHomeScreenDesign() {
    composeTestRule.setContent {
      MaterialTheme {
        Chip(
          text = AnnotatedString("Chip"),
          onClick = {}
        )
      }
    }

    composeTestRule.eyesTest("Chip") {
      checkWindow("Standard Chip")
    }
  }
}
