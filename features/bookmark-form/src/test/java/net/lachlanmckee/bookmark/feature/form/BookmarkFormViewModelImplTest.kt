package net.lachlanmckee.bookmark.feature.form

import app.cash.turbine.test
import io.mockk.Called
import io.mockk.coVerify
import io.mockk.mockk
import net.lachlanmckee.bookmark.feature.model.Navigation
import net.lachlanmckee.bookmark.service.repository.BookmarkRepository
import net.lachlanmckee.bookmark.test.util.flow.assertItem
import net.lachlanmckee.bookmark.test.util.flow.suspendTest
import org.junit.jupiter.api.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
internal class BookmarkFormViewModelImplTest {
  private val bookmarkRepository: BookmarkRepository = mockk(relaxed = true)

  private val homeViewModel = BookmarkFormViewModelImpl(
    bookmarkRepository
  )

  @Test
  fun givenNoState_whenSave_thenNotSaveBookmarkOrNavigateBack() = suspendTest {
    homeViewModel.navigation.test {
      homeViewModel.eventConsumer(BookmarkFormViewModel.Event.Save)
      expectNoEvents()
      cancel()
    }
    coVerify { bookmarkRepository wasNot Called }
  }

  @Test
  fun givenNoState_whenSave_thenSaveBookmarkAndNavigateBack() = suspendTest {
    homeViewModel.eventConsumer(BookmarkFormViewModel.Event.NameUpdated("Name"))
    homeViewModel.eventConsumer(BookmarkFormViewModel.Event.UrlUpdated("Url"))

    homeViewModel.navigation.test {
      homeViewModel.eventConsumer(BookmarkFormViewModel.Event.Save)
      assertItem(Navigation.Back)
      cancel()
    }
    coVerify { bookmarkRepository.saveBookmark("Name", "Url") }
  }
}
