package net.lachlanmckee.bookmark.feature.home

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.lachlanmckee.bookmark.feature.Navigator
import net.lachlanmckee.bookmark.service.repository.BookmarkRepository
import net.lachlanmckee.bookmark.service.repository.FolderRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(value = [ImmediateCoroutineExtension::class, InstantExecutorExtension::class])
@ExperimentalStdlibApi
@ExperimentalCoroutinesApi
class HomeViewModelTest {
    private val folderRepository: FolderRepository = mockk()
    private val bookmarkRepository: BookmarkRepository = mockk()
    private val navigator: Navigator = mockk()

    private val homeViewModel = HomeViewModel(
        folderRepository, bookmarkRepository, navigator
    )

    @Test
    fun givenNoState_whenBackPressed_thenNavigateBack() {
        every { navigator.back() } returns Unit
        homeViewModel.backPressed()
        verify { navigator.back() }
    }

    @Test
    fun givenState_when_then() {
        assertEquals(HomeViewModel.State.Empty, homeViewModel.state.getOrAwaitValue())
    }
}