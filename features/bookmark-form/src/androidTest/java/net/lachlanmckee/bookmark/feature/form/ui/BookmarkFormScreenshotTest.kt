package net.lachlanmckee.bookmark.feature.form.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.junit4.createComposeRule
import com.karumi.shot.ScreenshotTest
import kotlinx.coroutines.flow.MutableStateFlow
import net.lachlanmckee.bookmark.feature.form.BookmarkFormViewModel.State
import org.junit.Rule
import org.junit.Test

class BookmarkFormScreenshotTest : ScreenshotTest {

  @get:Rule
  val composeRule = createComposeRule()

  @Test
  fun verifyBookmarkFormScreenDesign() {
    val state: MutableStateFlow<State> = MutableStateFlow(State.emptyState)

    composeRule.setContent {
      MaterialTheme {
        BookmarkFormScreen(state) {}
      }
    }

    compareScreenshot(composeRule, "Empty")

    state.value = State(name = "Bookmark", url = "https://www.bookmark.com/", loading = false)
    compareScreenshot(composeRule, "With_Content")

    state.value = State(name = "Bookmark", url = "https://www.bookmark.com/", loading = true)
    compareScreenshot(composeRule, "With_Content_Saving")
  }
}
