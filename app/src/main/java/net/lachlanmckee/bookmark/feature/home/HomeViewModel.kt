package net.lachlanmckee.bookmark.feature.home

import net.lachlanmckee.bookmark.feature.BookmarkViewModel

interface HomeViewModel : BookmarkViewModel<HomeViewModel.State, HomeViewModel.Event> {

  sealed class State {
    object Empty : State()
    data class BookmarksExist(
      val contentList: List<Content>,
      val isInEditMode: Boolean
    ) : State()
  }

  sealed class Event {
    object Back : Event()
    object Delete : Event()
    object HomeClicked : Event()
    object SearchClicked : Event()
    object ResetDataClicked : Event()
    object SettingsClicked : Event()

    data class ContentClicked(val content: Content) : Event()
    data class ContentLongClicked(val content: Content) : Event()
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
      val link: String,
      val metadata: List<Metadata>
    ) : Content() {
      data class Metadata(
        val id: Long,
        val name: String
      )
    }
  }

  data class FolderMetadata(
    val folderId: Long,
    val parent: FolderMetadata?
  )
}
