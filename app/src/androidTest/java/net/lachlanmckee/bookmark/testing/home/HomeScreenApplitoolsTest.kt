package net.lachlanmckee.bookmark.testing.home

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import net.lachlanmckee.bookmark.HiltTestActivity
import net.lachlanmckee.bookmark.feature.home.HomeScreen
import net.lachlanmckee.bookmark.feature.home.HomeViewModel
import net.lachlanmckee.bookmark.testing.util.annotation.ApplitoolsTest
import net.lachlanmckee.bookmark.testing.util.eyes.eyesTest
import org.junit.Rule
import org.junit.Test

@ApplitoolsTest
@HiltAndroidTest
class HomeScreenApplitoolsTest {

  @get:Rule
  val composeTestRule =
    createAndroidComposeRule<HiltTestActivity>()

  @get:Rule
  val hiltRule = HiltAndroidRule(this)

  @Test
  fun verifyHomeScreenDesign() {
    hiltRule.inject()

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
