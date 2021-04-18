package net.lachlanmckee.bookmark.feature.home.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.junit4.createComposeRule
import com.karumi.shot.ScreenshotTest
import kotlinx.coroutines.flow.MutableStateFlow
import net.lachlanmckee.bookmark.feature.home.HomeViewModel
import net.lachlanmckee.bookmark.feature.home.model.HomeContent
import org.junit.Rule
import org.junit.Test

class HomeScreenScreenshotTest : ScreenshotTest {

  @get:Rule
  val composeRule = createComposeRule()

  @Test
  fun verifyHomeScreenDesign() {
    val state: MutableStateFlow<HomeViewModel.State> = MutableStateFlow(HomeViewModel.State.Empty)

    composeRule.setContent {
      MaterialTheme {
        HomeScreen(state) {}
      }
    }

    compareScreenshot(composeRule, "Empty")

    state.value = HomeViewModel.State.NoBookmarks(isRootFolder = true)
    compareScreenshot(composeRule, "No_Bookmarks")

    state.value = HomeViewModel.State.NoBookmarks(isRootFolder = false)
    compareScreenshot(composeRule, "No_Bookmarks_Not_root")

    state.value =
      HomeViewModel.State.BookmarksExist(
        contentList = listOf(
          HomeContent.FolderContent(
            id = 1,
            name = "Folder 1",
            selected = false
          ),
          HomeContent.BookmarkContent(
            id = 1,
            name = "Bookmark 1",
            selected = false,
            link = "Link",
            metadata = emptyList()
          )
        ),
        isInEditMode = false,
        isRootFolder = true
      )

    compareScreenshot(composeRule, "Non_Empty_Non_Selected")

    state.value =
      HomeViewModel.State.BookmarksExist(
        contentList = listOf(
          HomeContent.FolderContent(
            id = 1,
            name = "Folder 1",
            selected = true
          ),
          HomeContent.BookmarkContent(
            id = 1,
            name = "Bookmark 1",
            selected = true,
            link = "Link",
            metadata = emptyList()
          )
        ),
        isInEditMode = true,
        isRootFolder = true
      )

    compareScreenshot(composeRule, "Non_Empty_Selected")
  }
}
