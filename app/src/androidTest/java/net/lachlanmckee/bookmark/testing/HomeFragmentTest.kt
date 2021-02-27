package net.lachlanmckee.bookmark.testing

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import net.lachlanmckee.bookmark.HiltTestActivity
import net.lachlanmckee.bookmark.feature.home.HomeScreen
import net.lachlanmckee.bookmark.feature.home.HomeViewModel
import net.lachlanmckee.bookmark.service.persistence.dao.BookmarkDao
import net.lachlanmckee.bookmark.service.persistence.dao.FolderDao
import net.lachlanmckee.bookmark.service.persistence.entity.BookmarkEntity
import net.lachlanmckee.bookmark.service.persistence.entity.FolderEntity
import net.lachlanmckee.bookmark.service.repository.BookmarkRepository
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class HomeFragmentTest {

  @get:Rule
  val composeTestRule =
    createAndroidComposeRule<HiltTestActivity>()

  @get:Rule
  val hiltRule = HiltAndroidRule(this)

  @Inject
  lateinit var bookmarkRepository: BookmarkRepository

  @Inject
  lateinit var bookmarkDao: BookmarkDao

  @Inject
  lateinit var folderDao: FolderDao

  @ExperimentalCoroutinesApi
  @ExperimentalStdlibApi
  @Test
  fun givenValidLinkCopiedBeforeLaunch_whenLaunch_thenExpectCopyLink() {
    hiltRule.inject()

    runBlocking {
      folderDao.insertAll(FolderEntity(null, "Folder1"))
      bookmarkDao.insert(BookmarkEntity(1, "Bookmark1", "", null))
    }

    composeTestRule.setContent {
      MaterialTheme {
        HomeScreen(HomeViewModel(bookmarkRepository, mockk()))
      }
    }
  }
}
