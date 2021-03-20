package net.lachlanmckee.bookmark.feature.home

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import net.lachlanmckee.bookmark.feature.Navigation
import net.lachlanmckee.bookmark.feature.home.HomeViewModel.*
import net.lachlanmckee.bookmark.service.model.FolderContentModel
import net.lachlanmckee.bookmark.service.repository.BookmarkRepository
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModelImpl @Inject constructor(
  private val bookmarkRepository: BookmarkRepository
) : ViewModel(), HomeViewModel {

  private val navigationSharedFlow = MutableSharedFlow<Navigation>()
  private val currentFolderFlowable: MutableStateFlow<FolderMetadata?> = MutableStateFlow(null)

  private val editStateFlowable = MutableStateFlow(
    EditState(
      isInEditMode = false,
      selectedFolderIds = emptySet(),
      selectedBookmarkIds = emptySet()
    )
  )

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

  override val eventConsumer: (Event) -> Unit = { event ->
    viewModelScope.launch {
      when (event) {
        is Event.Back -> backPressed()
        is Event.ContentClicked -> contentClicked(event.content)
        is Event.ContentLongClicked -> contentLongClicked(event.content)
        is Event.Delete -> deleteClicked()
        is Event.HomeClicked -> navigationSharedFlow.emit(Navigation.Home)
        is Event.ResetDataClicked -> resetData()
        is Event.SearchClicked -> navigationSharedFlow.emit(Navigation.Search)
        is Event.SettingsClicked -> navigationSharedFlow.emit(Navigation.Settings)
      }
    }
  }

  override val navigation: Flow<Navigation>
    get() = navigationSharedFlow

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
          viewModelScope.launch {
            navigationSharedFlow.emit(Navigation.Bookmark(content.link))
          }
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
        viewModelScope.launch {
          navigationSharedFlow.emit(Navigation.Back)
        }
      }
    }
  }

  private data class EditState(
    val isInEditMode: Boolean,
    val selectedFolderIds: Set<Long>,
    val selectedBookmarkIds: Set<Long>
  )
}
