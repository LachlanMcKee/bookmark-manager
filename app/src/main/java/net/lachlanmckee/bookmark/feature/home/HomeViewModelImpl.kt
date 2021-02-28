package net.lachlanmckee.bookmark.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import net.lachlanmckee.bookmark.feature.Navigator
import net.lachlanmckee.bookmark.feature.home.HomeViewModel.*
import net.lachlanmckee.bookmark.service.model.FolderContentModel
import net.lachlanmckee.bookmark.service.repository.BookmarkRepository
import javax.inject.Inject

class HomeViewModelImpl @Inject constructor(
  private val bookmarkRepository: BookmarkRepository,
  private val navigator: Navigator
) : ViewModel(), HomeViewModel {

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
  override val state: LiveData<State> by lazy {
    currentFolderFlowable
      .flatMapLatest { folderMetadata ->
        val folderId = folderMetadata?.folderId
        combine(
          bookmarkRepository.getFolderContent(folderId),
          editStateFlowable,
          ::Pair
        )
      }
      .map { (bookmarkContent, editState) ->
        if (bookmarkContent.isNotEmpty()) {
          val contentList = bookmarkContent.map { contentModel ->
            when (contentModel) {
              is FolderContentModel.Folder -> {
                Content.FolderContent(
                  id = contentModel.folder.id,
                  name = contentModel.folder.name,
                  selected = editState.selectedFolderIds.contains(contentModel.folder.id)
                )
              }
              is FolderContentModel.Bookmark -> {
                Content.BookmarkContent(
                  id = contentModel.bookmark.id,
                  name = contentModel.bookmark.name,
                  selected = editState.selectedBookmarkIds.contains(contentModel.bookmark.id),
                  link = contentModel.bookmark.link,
                  metadata = contentModel.bookmark.metadata.map {
                    Content.BookmarkContent.Metadata(it.id, it.name)
                  }
                )
              }
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

  override fun invoke(event: Event) {
    when (event) {
      Event.Back -> backPressed()
      is Event.ContentClicked -> contentClicked(event.content)
      is Event.ContentLongClicked -> contentLongClicked(event.content)
      Event.Delete -> deleteClicked()
      Event.HomeClicked -> navigator.home()
      Event.ResetDataClicked -> resetData()
      Event.SearchClicked -> navigator.search()
      Event.SettingsClicked -> navigator.settings()
    }
  }

  private fun resetData() {
    viewModelScope.launch {
      bookmarkRepository.resetData()
    }
  }

  private fun contentClicked(content: Content) {
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

  private fun contentLongClicked(content: Content) {
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

  private fun deleteClicked() {
    val currentEditState = editStateFlowable.value
    if (currentEditState.isInEditMode) {
      viewModelScope.launch {
        bookmarkRepository.removeContent(
          folderIds = currentEditState.selectedFolderIds,
          bookmarkIds = currentEditState.selectedBookmarkIds
        )
      }
    }
  }

  private fun backPressed() {
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

  private data class EditState(
    val isInEditMode: Boolean,
    val selectedFolderIds: Set<Long>,
    val selectedBookmarkIds: Set<Long>
  )
}
