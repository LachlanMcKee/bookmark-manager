package net.lachlanmckee.bookmark.components.chip

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.AnnotatedString
import com.karumi.shot.ScreenshotTest
import org.junit.Rule
import org.junit.Test

class ChipApplitoolsTest : ScreenshotTest {

  @get:Rule
  val composeRule = createComposeRule()

  @Test
  fun verifyHomeScreenDesign() {
    composeRule.setContent {
      MaterialTheme {
        Chip(
          text = AnnotatedString("Chip"),
          onClick = {}
        )
      }
    }

    compareScreenshot(composeRule, "Standard_Chip")
  }
}
