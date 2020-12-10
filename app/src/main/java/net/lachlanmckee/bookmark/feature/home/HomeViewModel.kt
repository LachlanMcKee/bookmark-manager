package net.lachlanmckee.bookmark.feature.home

import androidx.lifecycle.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import net.lachlanmckee.bookmark.feature.Navigator
import net.lachlanmckee.bookmark.feature.RootViewModel
import net.lachlanmckee.bookmark.service.repository.BookmarkRepository
import net.lachlanmckee.bookmark.service.repository.FolderRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor(
  private val folderRepository: FolderRepository,
  private val bookmarkRepository: BookmarkRepository,
  private val navigator: Navigator
) : ViewModel(), RootViewModel {

  private val currentFolderFlowable: MutableStateFlow<FolderMetadata?> = MutableStateFlow(null)

  private val editStateFlowable = MutableStateFlow(
    EditState(
      isInEditMode = false,
      selectedFolderIds = emptySet(),
      selectedBookmarkIds = emptySet()
    )
  )

  @ExperimentalCoroutinesApi
  @ExperimentalStdlibApi
  val state: LiveData<State> by lazy {
    currentFolderFlowable
      .flatMapLatest { folderMetadata ->
        val folderId = folderMetadata?.folderId
        combine(
          folderRepository.getFolders(folderId),
          bookmarkRepository.getBookmarksByFolder(folderId),
          editStateFlowable,
          ::Triple
        )
      }
      .map { (folders, bookmarks, editState) ->
        if (folders.isNotEmpty() || bookmarks.isNotEmpty()) {
          val contentList = buildList {
            folders.forEach { folder ->
              add(
                Content.FolderContent(
                  id = folder.id,
                  name = folder.name,
                  selected = editState.selectedFolderIds.contains(folder.id)
                )
              )
            }
            bookmarks.forEach { bookmark ->
              add(
                Content.BookmarkContent(
                  id = bookmark.id,
                  name = bookmark.name,
                  selected = editState.selectedBookmarkIds.contains(bookmark.id),
                  link = bookmark.link
                )
              )
            }
          }
          State.BookmarksExist(
            contentList = contentList,
            isInEditMode = editState.isInEditMode
          )
        } else {
          State.Empty
        }
      }
      .asLiveData(viewModelScope.coroutineContext)
  }

  fun resetData() {
    viewModelScope.launch {
      bookmarkRepository.addBookmark()
      folderRepository.addFolder()
    }
  }

  fun contentClicked(content: Content) {
    val currentEditState = editStateFlowable.value
    if (currentEditState.isInEditMode) {
      editStateFlowable.value = currentEditState.copy(
        selectedFolderIds = currentEditState.selectedFolderIds
          .let { selectedIds ->
            if (content is Content.FolderContent) {
              if (selectedIds.contains(content.id)) {
                selectedIds.minus(content.id)
              } else {
                selectedIds.plus(content.id)
              }
            } else {
              selectedIds
            }
          },
        selectedBookmarkIds = currentEditState.selectedBookmarkIds
          .let { selectedIds ->
            if (content is Content.BookmarkContent) {
              if (selectedIds.contains(content.id)) {
                selectedIds.minus(content.id)
              } else {
                selectedIds.plus(content.id)
              }
            } else {
              selectedIds
            }
          }
      )
    } else {
      when (content) {
        is Content.FolderContent -> {
          currentFolderFlowable.value = FolderMetadata(
            content.id,
            currentFolderFlowable.value
          )
        }
        is Content.BookmarkContent -> {
          navigator.openBookmark(content.link)
        }
      }
    }
  }

  fun contentLongClicked(content: Content) {
    val currentEditState = editStateFlowable.value
    if (!currentEditState.isInEditMode) {
      val selectedFolderIds = if (content is Content.FolderContent) {
        setOf(content.id)
      } else {
        emptySet()
      }
      val selectedBookmarkIds = if (content is Content.BookmarkContent) {
        setOf(content.id)
      } else {
        emptySet()
      }
      editStateFlowable.value = EditState(
        isInEditMode = true,
        selectedFolderIds = selectedFolderIds,
        selectedBookmarkIds = selectedBookmarkIds
      )
    }
  }

  override fun homeClicked() {
    navigator.home()
  }

  override fun searchClicked() {
    navigator.search()
  }

  override fun settingsClicked() {
    navigator.settings()
  }

  fun deleteClicked() {
    val currentEditState = editStateFlowable.value
    if (currentEditState.isInEditMode) {
      viewModelScope.launch {
        folderRepository.removeFolders(currentEditState.selectedFolderIds)
        bookmarkRepository.removeBookmarks(currentEditState.selectedBookmarkIds)
      }
    }
  }

  fun backPressed() {
    if (editStateFlowable.value.isInEditMode) {
      editStateFlowable.value = EditState(
        isInEditMode = false,
        selectedFolderIds = emptySet(),
        selectedBookmarkIds = emptySet()
      )
    } else {
      if (currentFolderFlowable.value != null) {
        currentFolderFlowable.value = currentFolderFlowable.value?.parent
      } else {
        navigator.back()
      }
    }
  }

  sealed class State {
    object Empty : State()
    data class BookmarksExist(
      val contentList: List<Content>,
      val isInEditMode: Boolean
    ) : State()
  }

  sealed class Content {
    abstract val id: Long
    abstract val name: String
    abstract val selected: Boolean

    data class FolderContent(
      override val id: Long,
      override val name: String,
      override val selected: Boolean
    ) : Content()

    data class BookmarkContent(
      override val id: Long,
      override val name: String,
      override val selected: Boolean,
      val link: String
    ) : Content()
  }

  data class FolderMetadata(
    val folderId: Long,
    val parent: FolderMetadata?
  )

  private data class EditState(
    val isInEditMode: Boolean,
    val selectedFolderIds: Set<Long>,
    val selectedBookmarkIds: Set<Long>
  )
}
