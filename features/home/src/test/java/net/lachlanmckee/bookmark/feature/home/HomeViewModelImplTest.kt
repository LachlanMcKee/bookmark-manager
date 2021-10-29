package net.lachlanmckee.bookmark.feature.home

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import net.lachlanmckee.bookmark.feature.home.HomeViewModel.Event
import net.lachlanmckee.bookmark.feature.home.HomeViewModel.State.BookmarksExist
import net.lachlanmckee.bookmark.feature.home.model.HomeContent.BookmarkContent
import net.lachlanmckee.bookmark.feature.home.model.HomeContent.FolderContent
import net.lachlanmckee.bookmark.feature.model.Navigation
import net.lachlanmckee.bookmark.service.model.BookmarkModel
import net.lachlanmckee.bookmark.service.model.FolderContentModel
import net.lachlanmckee.bookmark.service.model.FolderItemModel
import net.lachlanmckee.bookmark.service.model.FolderModel
import net.lachlanmckee.bookmark.service.repository.BookmarkRepository
import net.lachlanmckee.bookmark.test.util.flow.assertItem
import net.lachlanmckee.bookmark.test.util.flow.suspendTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
class HomeViewModelImplTest {
  private val bookmarkRepository: BookmarkRepository = mockk(relaxed = true)
  private val savedStateHandle = mockk<SavedStateHandle>(relaxed = true) {
    every { get<String>("folderId") } returns null
  }

  private val homeViewModel = HomeViewModelImpl(
    bookmarkRepository,
    savedStateHandle
  )

  @Test
  fun givenNoState_whenBackPressed_thenNavigateBack() = suspendTest {
    homeViewModel.navigation.test {
      homeViewModel.eventConsumer(Event.Back)
      assertItem(Navigation.Back)
      cancel()
    }
  }

  @Test
  fun givenNoState_whenAddPressed_thenNavigateAddBookmark() = suspendTest {
    homeViewModel.navigation.test {
      homeViewModel.eventConsumer(Event.Add)
      assertItem(Navigation.AddBookmark)
      cancel()
    }
  }

  @Test
  fun givenNoState_whenHomePressed_thenNavigateToHome() = suspendTest {
    homeViewModel.navigation.test {
      homeViewModel.eventConsumer(Event.HomeClicked)
      assertItem(Navigation.Home)
      cancel()
    }
  }

  @Test
  fun givenNoState_whenSettingsPressed_thenNavigateToSettings() = suspendTest {
    homeViewModel.navigation.test {
      homeViewModel.eventConsumer(Event.SettingsClicked)
      assertItem(Navigation.Settings)
      cancel()
    }
  }

  @Test
  fun givenFolderAndBookmarksDoNotExist_whenStateObserved_thenExpectEmptyState() =
    suspendTest(startDispatcher = false) {
      givenFolderContent(null, emptyList())
      assertEquals(HomeViewModel.State.Loading(isRootFolder = true), homeViewModel.state.value)
    }

  @Test
  fun whenBookmarkClicked_thenNavigateToBookmark() = suspendTest {
    homeViewModel.navigation.test {
      homeViewModel.eventConsumer(Event.ContentClicked(unselectedBookmarkContent1))
      assertItem(Navigation.Bookmark("https://www.google.com/"))
      cancel()
    }
  }

  @Test
  fun whenFolderClicked_thenNavigateToFolder() = suspendTest {
    homeViewModel.navigation.test {
      homeViewModel.eventConsumer(Event.ContentClicked(unselectedFolderContent1))
      assertItem(Navigation.Folder("1"))
      cancel()
    }
  }

  @Test
  fun givenFolderAndBookmarksExists_whenStateObserved_thenExpectBookmarksExistState() =
    suspendTest {
      givenFolderContent(
        parentFolder = null,
        folderItemModels = listOf(
          FolderItemModel.Folder(folder1),
          FolderItemModel.Bookmark(bookmark1)
        )
      )

      homeViewModel.state.test {
        assertItem(
          BookmarksExist(
            folderName = null,
            contentList = listOf(
              unselectedFolderContent1,
              unselectedBookmarkContent1
            ),
            isInEditMode = false,
            isRootFolder = true
          )
        )
      }
    }

  @Test
  fun givenFolderAndBookmarksExists_whenLongClickFolderAndClickBookmark_thenExpectEditMode() =
    suspendTest {
      givenFolderContent(
        parentFolder = null,
        folderItemModels = listOf(
          FolderItemModel.Folder(folder1),
          FolderItemModel.Bookmark(bookmark1)
        )
      )

      homeViewModel.state.test {
        assertItem(
          BookmarksExist(
            folderName = null,
            contentList = listOf(
              unselectedFolderContent1,
              unselectedBookmarkContent1
            ),
            isInEditMode = false,
            isRootFolder = true
          )
        )

        homeViewModel.eventConsumer(Event.ContentLongClicked(unselectedFolderContent1))

        assertItem(
          BookmarksExist(
            folderName = null,
            contentList = listOf(
              unselectedFolderContent1.copy(selected = true),
              unselectedBookmarkContent1
            ),
            isInEditMode = true,
            isRootFolder = true
          )
        )

        homeViewModel.eventConsumer(Event.ContentClicked(unselectedBookmarkContent1))

        assertItem(
          BookmarksExist(
            folderName = null,
            contentList = listOf(
              unselectedFolderContent1.copy(selected = true),
              unselectedBookmarkContent1.copy(selected = true)
            ),
            isInEditMode = true,
            isRootFolder = true
          )
        )

        homeViewModel.eventConsumer(Event.ContentClicked(unselectedFolderContent1))

        assertItem(
          BookmarksExist(
            folderName = null,
            contentList = listOf(
              unselectedFolderContent1,
              unselectedBookmarkContent1.copy(selected = true)
            ),
            isInEditMode = true,
            isRootFolder = true
          )
        )

        homeViewModel.eventConsumer(Event.ContentClicked(unselectedBookmarkContent1))

        assertItem(
          BookmarksExist(
            folderName = null,
            contentList = listOf(
              unselectedFolderContent1,
              unselectedBookmarkContent1
            ),
            isInEditMode = true,
            isRootFolder = true
          )
        )
      }
    }

  @Test
  fun givenInEditMode_whenBackPressed_thenExitEditMode() = suspendTest {
    givenFolderContent(
      parentFolder = null,
      folderItemModels = listOf(
        FolderItemModel.Folder(folder1),
        FolderItemModel.Bookmark(bookmark1)
      )
    )

    homeViewModel.state.test {
      assertItem(
        BookmarksExist(
          folderName = null,
          contentList = listOf(
            unselectedFolderContent1,
            unselectedBookmarkContent1
          ),
          isInEditMode = false,
          isRootFolder = true
        )
      )

      homeViewModel.eventConsumer(Event.ContentLongClicked(unselectedFolderContent1))

      assertItem(
        BookmarksExist(
          folderName = null,
          contentList = listOf(
            unselectedFolderContent1.copy(selected = true),
            unselectedBookmarkContent1
          ),
          isInEditMode = true,
          isRootFolder = true
        )
      )

      homeViewModel.eventConsumer(Event.Back)

      assertItem(
        BookmarksExist(
          folderName = null,
          contentList = listOf(
            unselectedFolderContent1,
            unselectedBookmarkContent1
          ),
          isInEditMode = false,
          isRootFolder = true
        )
      )
    }
  }

  @Test
  fun givenFolderAndBookmarksExists_whenLongClickBookmarkAndClickFolder_thenExpectEditMode() =
    suspendTest {
      givenFolderContent(
        parentFolder = null,
        folderItemModels = listOf(
          FolderItemModel.Folder(folder1),
          FolderItemModel.Bookmark(bookmark1)
        )
      )

      homeViewModel.state.test {
        assertItem(
          BookmarksExist(
            folderName = null,
            contentList = listOf(
              unselectedFolderContent1,
              unselectedBookmarkContent1
            ),
            isInEditMode = false,
            isRootFolder = true
          )
        )

        homeViewModel.eventConsumer(Event.ContentLongClicked(unselectedBookmarkContent1))

        assertItem(
          BookmarksExist(
            folderName = null,
            contentList = listOf(
              unselectedFolderContent1,
              unselectedBookmarkContent1.copy(selected = true)
            ),
            isInEditMode = true,
            isRootFolder = true
          )
        )

        homeViewModel.eventConsumer(Event.ContentClicked(unselectedFolderContent1))

        assertItem(
          BookmarksExist(
            folderName = null,
            contentList = listOf(
              unselectedFolderContent1.copy(selected = true),
              unselectedBookmarkContent1.copy(selected = true)
            ),
            isInEditMode = true,
            isRootFolder = true
          )
        )
      }
    }

  @Test
  fun givenInEditMode_whenDeletePressed_thenRemoveFoldersAndBookmarks() = suspendTest {
    givenFolderContent(
      parentFolder = null,
      folderItemModels = listOf(
        FolderItemModel.Folder(folder1),
        FolderItemModel.Bookmark(bookmark1)
      )
    )

    homeViewModel.eventConsumer(Event.ContentLongClicked(unselectedFolderContent1))
    homeViewModel.eventConsumer(Event.ContentClicked(unselectedBookmarkContent1))
    homeViewModel.eventConsumer(Event.Delete)

    coVerify {
      bookmarkRepository.removeContent(
        folderIds = eq(setOf(1)),
        bookmarkIds = eq(setOf(10))
      )
    }
  }

  private fun givenFolderContent(
    parentFolder: FolderModel?,
    folderItemModels: List<FolderItemModel>
  ) {
    every { bookmarkRepository.getFolderContent(parentFolder?.id) } returns flowOf(
      FolderContentModel(
        folder = parentFolder,
        items = folderItemModels
      )
    )
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
