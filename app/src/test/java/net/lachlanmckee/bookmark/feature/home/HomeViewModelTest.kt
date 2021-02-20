package net.lachlanmckee.bookmark.feature.home

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import net.lachlanmckee.bookmark.feature.Navigator
import net.lachlanmckee.bookmark.feature.home.HomeViewModel.Content.BookmarkContent
import net.lachlanmckee.bookmark.feature.home.HomeViewModel.Content.FolderContent
import net.lachlanmckee.bookmark.feature.home.HomeViewModel.State.*
import net.lachlanmckee.bookmark.service.model.BookmarkModel
import net.lachlanmckee.bookmark.service.model.FolderContentModel
import net.lachlanmckee.bookmark.service.model.FolderModel
import net.lachlanmckee.bookmark.service.repository.BookmarkRepository
import net.lachlanmckee.bookmark.test.util.LiveDataTester.testLiveData
import net.lachlanmckee.bookmark.test.util.getOrAwaitValue
import net.lachlanmckee.bookmark.test.util.getOrAwaitValues
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@ExperimentalStdlibApi
@ExperimentalCoroutinesApi
class HomeViewModelTest {
  private val bookmarkRepository: BookmarkRepository = mockk(relaxed = true)
  private val navigator: Navigator = mockk(relaxed = true)

  private val homeViewModel = HomeViewModel(
    bookmarkRepository, navigator
  )

  @Test
  fun givenNoState_whenBackPressed_thenNavigateBack() {
    homeViewModel.backPressed()
    verify { navigator.back() }
  }

  @Test
  fun givenNoState_whenHomePressed_thenNavigateToHome() {
    homeViewModel.homeClicked()
    verify { navigator.home() }
  }

  @Test
  fun givenNoState_whenSettingsPressed_thenNavigateToSettings() {
    homeViewModel.settingsClicked()
    verify { navigator.settings() }
  }

  @Test
  fun givenFolderAndBookmarksDoNotExist_whenStateObserved_thenExpectEmptyState() = testLiveData {
    givenFolderContent(null, emptyList())
    assertEquals(Empty, homeViewModel.state.getOrAwaitValue())
  }

  @Test
  fun whenBookmarkClicked_thenNavigateToBookmark() = testLiveData {
    homeViewModel.contentClicked(unselectedBookmarkContent1)
    verify { navigator.openBookmark("https://www.google.com/") }
  }

  @Test
  fun whenFolderClicked_thenRequestNewDataAndShowNestedFolder() = testLiveData {
    givenFolderContent(
      parentId = null,
      folderContentModels = listOf(
        FolderContentModel.Folder(folder1),
        FolderContentModel.Bookmark(bookmark1)
      )
    )

    givenFolderContent(
      parentId = 1,
      folderContentModels = listOf(
        FolderContentModel.Bookmark(bookmark2)
      )
    )

    val statesList = homeViewModel.state.getOrAwaitValues(numberOfValues = 3) { nextStepIndex ->
      if (nextStepIndex == 1) {
        homeViewModel.contentClicked(unselectedFolderContent1)
      }
      if (nextStepIndex == 2) {
        homeViewModel.backPressed()
      }
    }

    assertEquals(
      listOf(
        BookmarksExist(
          contentList = listOf(
            unselectedFolderContent1,
            unselectedBookmarkContent1
          ),
          isInEditMode = false
        ),
        BookmarksExist(
          contentList = listOf(
            unselectedBookmarkContent2
          ),
          isInEditMode = false
        ),
        BookmarksExist(
          contentList = listOf(
            unselectedFolderContent1,
            unselectedBookmarkContent1
          ),
          isInEditMode = false
        )
      ),
      statesList
    )
  }

  @Test
  fun givenFolderAndBookmarksExists_whenStateObserved_thenExpectBookmarksExistState() =
    testLiveData {
      givenFolderContent(
        parentId = null,
        folderContentModels = listOf(
          FolderContentModel.Folder(folder1),
          FolderContentModel.Bookmark(bookmark1)
        )
      )

      assertEquals(
        BookmarksExist(
          contentList = listOf(
            unselectedFolderContent1,
            unselectedBookmarkContent1
          ),
          isInEditMode = false
        ),
        homeViewModel.state.getOrAwaitValue()
      )
    }

  @Test
  fun givenFolderAndBookmarksExists_whenLongClickFolderAndClickBookmark_thenExpectEditMode() =
    testLiveData {
      givenFolderContent(
        parentId = null,
        folderContentModels = listOf(
          FolderContentModel.Folder(folder1),
          FolderContentModel.Bookmark(bookmark1)
        )
      )

      val statesList =
        homeViewModel.state.getOrAwaitValues(numberOfValues = 5) { nextStepIndex ->
          if (nextStepIndex == 1) {
            homeViewModel.contentLongClicked(unselectedFolderContent1)
          }
          if (nextStepIndex == 2) {
            homeViewModel.contentClicked(unselectedBookmarkContent1)
          }
          if (nextStepIndex == 3) {
            homeViewModel.contentClicked(unselectedFolderContent1)
          }
          if (nextStepIndex == 4) {
            homeViewModel.contentClicked(unselectedBookmarkContent1)
          }
        }

      assertEquals(
        listOf(
          BookmarksExist(
            contentList = listOf(
              unselectedFolderContent1,
              unselectedBookmarkContent1
            ),
            isInEditMode = false
          ),
          BookmarksExist(
            contentList = listOf(
              unselectedFolderContent1.copy(selected = true),
              unselectedBookmarkContent1
            ),
            isInEditMode = true
          ),
          BookmarksExist(
            contentList = listOf(
              unselectedFolderContent1.copy(selected = true),
              unselectedBookmarkContent1.copy(selected = true)
            ),
            isInEditMode = true
          ),
          BookmarksExist(
            contentList = listOf(
              unselectedFolderContent1,
              unselectedBookmarkContent1.copy(selected = true)
            ),
            isInEditMode = true
          ),
          BookmarksExist(
            contentList = listOf(
              unselectedFolderContent1,
              unselectedBookmarkContent1
            ),
            isInEditMode = true
          )
        ),
        statesList
      )
    }

  @Test
  fun givenInEditMode_whenBackPressed_thenExitEditMode() = testLiveData {
    givenFolderContent(
      parentId = null,
      folderContentModels = listOf(
        FolderContentModel.Folder(folder1),
        FolderContentModel.Bookmark(bookmark1)
      )
    )

    val statesList = homeViewModel.state.getOrAwaitValues(numberOfValues = 3) { nextStepIndex ->
      if (nextStepIndex == 1) {
        homeViewModel.contentLongClicked(unselectedFolderContent1)
      }
      if (nextStepIndex == 2) {
        homeViewModel.backPressed()
      }
    }

    assertEquals(
      listOf(
        BookmarksExist(
          contentList = listOf(
            unselectedFolderContent1,
            unselectedBookmarkContent1
          ),
          isInEditMode = false
        ),
        BookmarksExist(
          contentList = listOf(
            unselectedFolderContent1.copy(selected = true),
            unselectedBookmarkContent1
          ),
          isInEditMode = true
        ),
        BookmarksExist(
          contentList = listOf(
            unselectedFolderContent1,
            unselectedBookmarkContent1
          ),
          isInEditMode = false
        )
      ),
      statesList
    )
  }

  @Test
  fun givenFolderAndBookmarksExists_whenLongClickBookmarkAndClickFolder_thenExpectEditMode() =
    testLiveData {
      givenFolderContent(
        parentId = null,
        folderContentModels = listOf(
          FolderContentModel.Folder(folder1),
          FolderContentModel.Bookmark(bookmark1)
        )
      )

      val statesList =
        homeViewModel.state.getOrAwaitValues(numberOfValues = 3) { nextStepIndex ->
          if (nextStepIndex == 1) {
            homeViewModel.contentLongClicked(unselectedBookmarkContent1)
          }
          if (nextStepIndex == 2) {
            homeViewModel.contentClicked(unselectedFolderContent1)
          }
        }

      assertEquals(
        listOf(
          BookmarksExist(
            contentList = listOf(
              unselectedFolderContent1,
              unselectedBookmarkContent1
            ),
            isInEditMode = false
          ),
          BookmarksExist(
            contentList = listOf(
              unselectedFolderContent1,
              unselectedBookmarkContent1.copy(selected = true)
            ),
            isInEditMode = true
          ),
          BookmarksExist(
            contentList = listOf(
              unselectedFolderContent1.copy(selected = true),
              unselectedBookmarkContent1.copy(selected = true)
            ),
            isInEditMode = true
          )
        ),
        statesList
      )
    }

  @Disabled("Currently mockk doesn't work with verify coroutines.")
  @Test
  fun givenInEditMode_whenDeletePressed_thenRemoveFoldersAndBookmarks() = testLiveData {
    givenFolderContent(
      parentId = null,
      folderContentModels = listOf(
        FolderContentModel.Folder(folder1),
        FolderContentModel.Bookmark(bookmark1)
      )
    )

    homeViewModel.state.getOrAwaitValues(numberOfValues = 3) { nextStepIndex ->
      if (nextStepIndex == 1) {
        homeViewModel.contentLongClicked(unselectedFolderContent1)
      }
      if (nextStepIndex == 2) {
        homeViewModel.contentClicked(unselectedBookmarkContent1)
      }
    }

    homeViewModel.deleteClicked()

    coVerify {
      bookmarkRepository.removeContent(
        folderIds = eq(setOf(1)),
        bookmarkIds = eq(setOf(1))
      )
    }
  }

  private fun givenFolderContent(parentId: Long?, folderContentModels: List<FolderContentModel>) {
    every { bookmarkRepository.getFolderContent(parentId) } returns flowOf(folderContentModels)
  }

  private companion object {
    private val folder1 = FolderModel(id = 1, parentId = null, name = "folder1")
    private val bookmark1 = BookmarkModel(
      id = 10,
      name = "Bookmark1",
      link = "https://www.google.com/",
      metadata = emptyList()
    )
    private val bookmark2 = BookmarkModel(
      id = 11,
      name = "Bookmark2",
      link = "https://www.android.com/",
      metadata = emptyList()
    )

    private val unselectedFolderContent1 = FolderContent(
      id = 1,
      name = "folder1",
      selected = false
    )
    private val unselectedBookmarkContent1 = BookmarkContent(
      id = 10,
      name = "Bookmark1",
      selected = false,
      link = "https://www.google.com/",
      metadata = emptyList()
    )
    private val unselectedBookmarkContent2 = BookmarkContent(
      id = 11,
      name = "Bookmark2",
      selected = false,
      link = "https://www.android.com/",
      metadata = emptyList()
    )
  }
}
