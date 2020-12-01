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
import net.lachlanmckee.bookmark.service.model.Bookmark
import net.lachlanmckee.bookmark.service.model.Folder
import net.lachlanmckee.bookmark.service.repository.BookmarkRepository
import net.lachlanmckee.bookmark.service.repository.FolderRepository
import net.lachlanmckee.bookmark.test.util.LiveDataTester.testLiveData
import net.lachlanmckee.bookmark.test.util.getOrAwaitValue
import net.lachlanmckee.bookmark.test.util.getOrAwaitValues
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@ExperimentalStdlibApi
@ExperimentalCoroutinesApi
class HomeViewModelTest {
  private val folderRepository: FolderRepository = mockk(relaxed = true)
  private val bookmarkRepository: BookmarkRepository = mockk(relaxed = true)
  private val navigator: Navigator = mockk(relaxed = true)

  private val homeViewModel = HomeViewModel(
    folderRepository, bookmarkRepository, navigator
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
    givenFolders(null, emptyList())
    givenBookmarks(null, emptyList())
    assertEquals(Empty, homeViewModel.state.getOrAwaitValue())
  }

  @Test
  fun whenBookmarkClicked_thenNavigateToBookmark() = testLiveData {
    homeViewModel.contentClicked(unselectedBookmarkContent1)
    verify { navigator.openBookmark("https://www.google.com/") }
  }

  @Test
  fun whenFolderClicked_thenRequestNewDataAndShowNestedFolder() = testLiveData {
    givenFolders(null, listOf(folder1))
    givenBookmarks(null, listOf(bookmark1))

    givenFolders(1, emptyList())
    givenBookmarks(1, listOf(bookmark2))

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
      givenFolders(null, listOf(folder1))
      givenBookmarks(null, listOf(bookmark1))

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
      givenFolders(null, listOf(folder1))
      givenBookmarks(null, listOf(bookmark1))

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
    givenFolders(null, listOf(folder1))
    givenBookmarks(null, listOf(bookmark1))

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
      givenFolders(null, listOf(folder1))
      givenBookmarks(null, listOf(bookmark1))

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
    givenFolders(null, listOf(folder1))
    givenBookmarks(null, listOf(bookmark1))

    homeViewModel.state.getOrAwaitValues(numberOfValues = 3) { nextStepIndex ->
      if (nextStepIndex == 1) {
        homeViewModel.contentLongClicked(unselectedFolderContent1)
      }
      if (nextStepIndex == 2) {
        homeViewModel.contentClicked(unselectedBookmarkContent1)
      }
    }

    homeViewModel.deleteClicked()

    coVerify { folderRepository.removeFolders(eq(setOf(1))) }
    coVerify { bookmarkRepository.removeBookmarks(eq(setOf(1))) }
  }

  private fun givenFolders(parentId: Int?, folders: List<Folder>) {
    every { folderRepository.getFolders(parentId) } returns flowOf(folders)
  }

  private fun givenBookmarks(parentId: Int?, bookmarks: List<Bookmark>) {
    every { bookmarkRepository.getBookmarks(parentId) } returns flowOf(bookmarks)
  }

  private companion object {
    private val folder1 = Folder(id = 1, parentId = null, name = "folder1")
    private val bookmark1 = Bookmark(
      id = 10,
      name = "Bookmark1",
      link = "https://www.google.com/"
    )
    private val bookmark2 = Bookmark(
      id = 11,
      name = "Bookmark2",
      link = "https://www.android.com/"
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
      link = "https://www.google.com/"
    )
    private val unselectedBookmarkContent2 = BookmarkContent(
      id = 11,
      name = "Bookmark2",
      selected = false,
      link = "https://www.android.com/"
    )
  }
}
