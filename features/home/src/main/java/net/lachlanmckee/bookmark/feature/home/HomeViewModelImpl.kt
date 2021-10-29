package net.lachlanmckee.bookmark.feature.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import net.lachlanmckee.bookmark.feature.StandardViewModel
import net.lachlanmckee.bookmark.feature.home.HomeViewModel.Event
import net.lachlanmckee.bookmark.feature.home.HomeViewModel.State
import net.lachlanmckee.bookmark.feature.home.model.HomeContent
import net.lachlanmckee.bookmark.feature.model.Navigation
import net.lachlanmckee.bookmark.service.model.FolderItemModel
import net.lachlanmckee.bookmark.service.repository.BookmarkRepository
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModelImpl @Inject constructor(
  private val bookmarkRepository: BookmarkRepository,
  private val savedStateHandle: SavedStateHandle
) : StandardViewModel<State, Event>(), HomeViewModel {

  private val folderId by lazy { savedStateHandle.get<String>("folderId")?.toLong() }

  private val editStateFlowable = MutableStateFlow(
    EditState(
      isInEditMode = false,
      selectedFolderIds = emptySet(),
      selectedBookmarkIds = emptySet()
    )
  )

  override val initialState: State = State.Loading(isRootFolder = folderId == null)

  override fun createState(): Flow<State> =
    combine(
      bookmarkRepository.getFolderContent(folderId),
      editStateFlowable,
      ::Pair
    )
      .map { (bookmarkContent, editState) ->
        if (bookmarkContent.items.isNotEmpty()) {
          val contentList = bookmarkContent.items.map { contentModel ->
            when (contentModel) {
              is FolderItemModel.Folder -> {
                HomeContent.FolderContent(
                  id = contentModel.folder.id,
                  name = contentModel.folder.name,
                  selected = editState.selectedFolderIds.contains(contentModel.folder.id)
                )
              }
              is FolderItemModel.Bookmark -> {
                HomeContent.BookmarkContent(
                  id = contentModel.bookmark.id,
                  name = contentModel.bookmark.name,
                  selected = editState.selectedBookmarkIds.contains(contentModel.bookmark.id),
                  link = contentModel.bookmark.link,
                  metadata = contentModel.bookmark.metadata.map {
                    HomeContent.BookmarkContent.Metadata(it.id, it.name)
                  }
                )
              }
            }
          }
          State.BookmarksExist(
            folderName = bookmarkContent.folder?.name,
            contentList = contentList,
            isInEditMode = editState.isInEditMode,
            isRootFolder = folderId == null
          )
        } else {
          State.NoBookmarks(
            folderName = bookmarkContent.folder?.name,
            isRootFolder = folderId == null
          )
        }
      }

  override val eventConsumer: (Event) -> Unit = { event ->
    viewModelScope.launch {
      when (event) {
        is Event.Back -> backPressed()
        is Event.Add -> navigate(Navigation.AddBookmark)
        is Event.ContentClicked -> contentClicked(event.content)
        is Event.ContentLongClicked -> contentLongClicked(event.content)
        is Event.Delete -> deleteClicked()
        is Event.HomeClicked -> navigate(Navigation.Home)
        is Event.ResetDataClicked -> resetData()
        is Event.SearchClicked -> navigate(Navigation.Search)
        is Event.SettingsClicked -> navigate(Navigation.Settings)
      }
    }
  }

  private fun resetData() {
    viewModelScope.launch {
      bookmarkRepository.resetData()
    }
  }

  private fun contentClicked(content: HomeContent) {
    val currentEditState = editStateFlowable.value
    if (currentEditState.isInEditMode) {
      editStateFlowable.value = currentEditState.copy(
        selectedFolderIds = currentEditState.selectedFolderIds
          .let { selectedIds ->
            if (content is HomeContent.FolderContent) {
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
            if (content is HomeContent.BookmarkContent) {
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
        is HomeContent.FolderContent -> {
          navigate(Navigation.Folder(folderId = content.id.toString()))
        }
        is HomeContent.BookmarkContent -> {
          navigate(Navigation.Bookmark(content.link))
        }
      }
    }
  }

  private fun contentLongClicked(content: HomeContent) {
    val currentEditState = editStateFlowable.value
    if (!currentEditState.isInEditMode) {
      val selectedFolderIds = if (content is HomeContent.FolderContent) {
        setOf(content.id)
      } else {
        emptySet()
      }
      val selectedBookmarkIds = if (content is HomeContent.BookmarkContent) {
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
      navigate(Navigation.Back)
    }
  }

  private data class EditState(
    val isInEditMode: Boolean,
    val selectedFolderIds: Set<Long>,
    val selectedBookmarkIds: Set<Long>
  )
}
