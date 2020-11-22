package net.lachlanmckee.bookmark.feature.home

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import net.lachlanmckee.bookmark.feature.Navigator
import net.lachlanmckee.bookmark.service.model.Bookmark
import net.lachlanmckee.bookmark.service.repository.BookmarkRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
    private val navigator: Navigator
) : ViewModel() {

    private val bookmarksLiveData = bookmarkRepository
        .getBookmarks()
        .asLiveData(viewModelScope.coroutineContext)

    val state: LiveData<State>
        get() {
            return bookmarksLiveData
                .map { data ->
                    if (data.isNotEmpty()) {
                        State.BookmarksExist(bookmarks = data)
                    } else {
                        State.Empty
                    }
                }
        }

    fun addBookmark() {
        viewModelScope.launch {
            bookmarkRepository.addBookmark()
        }
    }

    fun bookmarkClicked(bookmark: Bookmark) {
        navigator.openBookmark(bookmark.link)
    }

    sealed class State {
        object Empty : State()
        data class BookmarksExist(val bookmarks: List<Bookmark>) : State()
    }
}
