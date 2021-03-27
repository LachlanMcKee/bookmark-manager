package net.lachlanmckee.bookmark.feature.home.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.MutableLiveData
import com.karumi.shot.ScreenshotTest
import net.lachlanmckee.bookmark.feature.home.HomeViewModel
import net.lachlanmckee.bookmark.feature.home.model.HomeContent
import org.junit.Rule
import org.junit.Test

class HomeScreenScreenshotTest : ScreenshotTest {

  @get:Rule
  val composeRule = createComposeRule()

  @Test
  fun verifyHomeScreenDesign() {
    val state: MutableLiveData<HomeViewModel.State> = MutableLiveData()

    composeRule.setContent {
      MaterialTheme {
        HomeScreen(state) {}
      }
    }

    state.postValue(
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
        isInEditMode = false
      )
    )

    compareScreenshot(composeRule, "Non_Empty_Non_Selected")

    state.postValue(
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
        isInEditMode = true
      )
    )

    compareScreenshot(composeRule, "Non_Empty_Selected")

    state.postValue(HomeViewModel.State.Empty)

    compareScreenshot(composeRule, "Empty")
  }
}
