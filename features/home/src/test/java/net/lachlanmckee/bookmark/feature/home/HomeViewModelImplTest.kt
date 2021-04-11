package net.lachlanmckee.bookmark.feature.home

import app.cash.turbine.test
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import net.lachlanmckee.bookmark.feature.home.HomeViewModel.State.BookmarksExist
import net.lachlanmckee.bookmark.feature.home.HomeViewModel.State.Empty
import net.lachlanmckee.bookmark.feature.home.model.HomeContent.BookmarkContent
import net.lachlanmckee.bookmark.feature.home.model.HomeContent.FolderContent
import net.lachlanmckee.bookmark.feature.model.Navigation
import net.lachlanmckee.bookmark.service.model.BookmarkModel
import net.lachlanmckee.bookmark.service.model.FolderContentModel
import net.lachlanmckee.bookmark.service.model.FolderModel
import net.lachlanmckee.bookmark.service.repository.BookmarkRepository
import net.lachlanmckee.bookmark.test.util.livedata.LiveDataTester.testLiveData
import net.lachlanmckee.bookmark.test.util.livedata.getOrAwaitValue
import net.lachlanmckee.bookmark.test.util.livedata.getOrAwaitValues
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
class HomeViewModelImplTest {
  private val bookmarkRepository: BookmarkRepository = mockk(relaxed = true)

  private val homeViewModel = HomeViewModelImpl(
    bookmarkRepository
  )

  @Test
  fun givenNoState_whenBackPressed_thenNavigateBack() = testLiveData {
    homeViewModel.navigation.test {
      homeViewModel.eventConsumer(HomeViewModel.Event.Back)
      assertEquals(Navigation.Back, expectItem())
      cancel()
    }
  }

  @Test
  fun givenNoState_whenHomePressed_thenNavigateToHome() = testLiveData {
    homeViewModel.navigation.test {
      homeViewModel.eventConsumer(HomeViewModel.Event.HomeClicked)
      assertEquals(Navigation.Home, expectItem())
      cancel()
    }
  }

  @Test
  fun givenNoState_whenSettingsPressed_thenNavigateToSettings() = testLiveData {
    homeViewModel.navigation.test {
      homeViewModel.eventConsumer(HomeViewModel.Event.SettingsClicked)
      assertEquals(Navigation.Settings, expectItem())
      cancel()
    }
  }

  @Test
  fun givenFolderAndBookmarksDoNotExist_whenStateObserved_thenExpectEmptyState() = testLiveData {
    givenFolderContent(null, emptyList())
    assertEquals(Empty, homeViewModel.state.getOrAwaitValue())
  }

  @Test
  fun whenBookmarkClicked_thenNavigateToBookmark() = testLiveData {
    homeViewModel.navigation.test {
      homeViewModel.eventConsumer(HomeViewModel.Event.ContentClicked(unselectedBookmarkContent1))
      assertEquals(Navigation.Bookmark("https://www.google.com/"), expectItem())
      cancel()
    }
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

    val statesList = homeViewModel.state.getOrAwaitValues(numberOfValues = 4) { nextStepIndex ->
      if (nextStepIndex == 2) {
        homeViewModel.eventConsumer(HomeViewModel.Event.ContentClicked(unselectedFolderContent1))
      }
      if (nextStepIndex == 3) {
        homeViewModel.eventConsumer(HomeViewModel.Event.Back)
      }
    }

    assertEquals(
      listOf(
        Empty,
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
        homeViewModel.state.getOrAwaitValues(numberOfValues = 2, executionFunc = {}).last()
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
        homeViewModel.state.getOrAwaitValues(numberOfValues = 6) { nextStepIndex ->
          if (nextStepIndex == 2) {
            homeViewModel.eventConsumer(
              HomeViewModel.Event.ContentLongClicked(
                unselectedFolderContent1
              )
            )
          }
          if (nextStepIndex == 3) {
            homeViewModel.eventConsumer(
              HomeViewModel.Event.ContentClicked(
                unselectedBookmarkContent1
              )
            )
          }
          if (nextStepIndex == 4) {
            homeViewModel.eventConsumer(HomeViewModel.Event.ContentClicked(unselectedFolderContent1))
          }
          if (nextStepIndex == 5) {
            homeViewModel.eventConsumer(
              HomeViewModel.Event.ContentClicked(
                unselectedBookmarkContent1
              )
            )
          }
        }

      assertEquals(
        listOf(
          Empty,
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

    val statesList = homeViewModel.state.getOrAwaitValues(numberOfValues = 4) { nextStepIndex ->
      if (nextStepIndex == 2) {
        homeViewModel.eventConsumer(HomeViewModel.Event.ContentLongClicked(unselectedFolderContent1))
      }
      if (nextStepIndex == 3) {
        homeViewModel.eventConsumer(HomeViewModel.Event.Back)
      }
    }

    assertEquals(
      listOf(
        Empty,
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
        homeViewModel.state.getOrAwaitValues(numberOfValues = 4) { nextStepIndex ->
          if (nextStepIndex == 2) {
            homeViewModel.eventConsumer(
              HomeViewModel.Event.ContentLongClicked(
                unselectedBookmarkContent1
              )
            )
          }
          if (nextStepIndex == 3) {
            homeViewModel.eventConsumer(HomeViewModel.Event.ContentClicked(unselectedFolderContent1))
          }
        }

      assertEquals(
        listOf(
          Empty,
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
        homeViewModel.eventConsumer(HomeViewModel.Event.ContentLongClicked(unselectedFolderContent1))
      }
      if (nextStepIndex == 2) {
        homeViewModel.eventConsumer(HomeViewModel.Event.ContentClicked(unselectedBookmarkContent1))
      }
    }

    homeViewModel.eventConsumer(HomeViewModel.Event.Delete)

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
