package net.lachlanmckee.bookmark.feature.home

import androidx.lifecycle.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import net.lachlanmckee.bookmark.feature.Navigator
import net.lachlanmckee.bookmark.feature.RootViewModel
import net.lachlanmckee.bookmark.service.repository.BookmarkRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
    private val navigator: Navigator
) : ViewModel(), RootViewModel {

    private val bookmarksStateFlow = bookmarkRepository.getBookmarks()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val editStateFlowable = MutableStateFlow(
        EditState(
            isInEditMode = false,
            selectedIds = emptySet()
        )
    )

    private val bookmarksLiveData = combine(
        bookmarksStateFlow, editStateFlowable, ::Pair
    ).asLiveData(viewModelScope.coroutineContext)

    val state: LiveData<State>
        get() {
            return bookmarksLiveData
                .map { (bookmarks, editState) ->
                    if (bookmarks.isNotEmpty()) {
                        State.BookmarksExist(
                            bookmarks = bookmarks.map {
                                BookMarkModel(
                                    it.id,
                                    it.name,
                                    it.link,
                                    editState.selectedIds.contains(it.id)
                                )
                            },
                            isInEditMode = editState.isInEditMode
                        )
                    } else {
                        State.Empty
                    }
                }
        }

    fun addBookmarkClicked() {
        viewModelScope.launch {
            bookmarkRepository.addBookmark()
        }
    }

    fun bookmarkClicked(bookmark: BookMarkModel) {
        val currentEditState = editStateFlowable.value
        if (currentEditState.isInEditMode) {
            editStateFlowable.value = currentEditState.copy(
                selectedIds = currentEditState.selectedIds
                    .let { selectedIds ->
                        if (selectedIds.contains(bookmark.id)) {
                            selectedIds.minus(bookmark.id)
                        } else {
                            selectedIds.plus(bookmark.id)
                        }
                    }
            )
        } else {
            navigator.openBookmark(bookmark.link)
        }
    }

    fun bookmarkLongClicked(bookmark: BookMarkModel) {
        val currentEditState = editStateFlowable.value
        if (!currentEditState.isInEditMode) {
            editStateFlowable.value = EditState(
                isInEditMode = true,
                selectedIds = setOf(bookmark.id)
            )
        }
    }

    override fun homeClicked() {
        navigator.home()
    }

    override fun settingsClicked() {
        navigator.settings()
    }

    fun deleteClicked() {
        val currentEditState = editStateFlowable.value
        if (currentEditState.isInEditMode) {
            viewModelScope.launch {
                bookmarkRepository.removeBookmarks(currentEditState.selectedIds)
            }
        }
    }

    fun backPressed() {
        if (editStateFlowable.value.isInEditMode) {
            editStateFlowable.value = EditState(
                isInEditMode = false,
                selectedIds = emptySet()
            )
        } else {
            navigator.back()
        }
    }

    sealed class State {
        object Empty : State()
        data class BookmarksExist(
            val bookmarks: List<BookMarkModel>,
            val isInEditMode: Boolean
        ) : State()
    }

    data class BookMarkModel(
        val id: Int,
        val name: String,
        val link: String,
        val selected: Boolean
    )

    private data class EditState(
        val isInEditMode: Boolean,
        val selectedIds: Set<Int>
    )
}
