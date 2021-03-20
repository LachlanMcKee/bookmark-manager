package net.lachlanmckee.bookmark.feature.home

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.MutableLiveData
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
    val state: MutableLiveData<HomeViewModel.State> = MutableLiveData()

    composeTestRule.setContent {
      MaterialTheme {
        HomeScreen(state) {}
      }
    }

    composeTestRule.eyesTest("HomeScreen") {
      state.postValue(
        HomeViewModel.State.BookmarksExist(
          contentList = listOf(
            HomeViewModel.Content.FolderContent(
              id = 1,
              name = "Folder 1",
              selected = false
            ),
            HomeViewModel.Content.BookmarkContent(
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

      checkWindow("Non-Empty, Non-Selected")

      state.postValue(
        HomeViewModel.State.BookmarksExist(
          contentList = listOf(
            HomeViewModel.Content.FolderContent(
              id = 1,
              name = "Folder 1",
              selected = true
            ),
            HomeViewModel.Content.BookmarkContent(
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

      checkWindow("Non-Empty, Selected")

      state.postValue(HomeViewModel.State.Empty)

      checkWindow("Empty")
    }
  }
}