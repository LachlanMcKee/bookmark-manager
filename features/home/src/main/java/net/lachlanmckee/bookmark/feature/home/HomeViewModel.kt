package net.lachlanmckee.bookmark.feature.home

import androidx.compose.runtime.Immutable
import net.lachlanmckee.bookmark.feature.BookmarkViewModel
import net.lachlanmckee.bookmark.feature.home.model.HomeContent

internal interface HomeViewModel : BookmarkViewModel<HomeViewModel.State, HomeViewModel.Event> {

  @Immutable
  sealed class State {
    abstract val folderName: String?
    abstract val isRootFolder: Boolean

    @Immutable
    data class Loading(
      override val isRootFolder: Boolean
    ) : State() {
      override val folderName: String? = null
    }

    @Immutable
    data class NoBookmarks(
      override val folderName: String?,
      override val isRootFolder: Boolean
    ) : State()

    @Immutable
    data class BookmarksExist(
      override val folderName: String?,
      val contentList: List<HomeContent>,
      val isInEditMode: Boolean,
      override val isRootFolder: Boolean
    ) : State()
  }

  sealed class Event {
    object Back : Event()
    object Add : Event()
    object Delete : Event()
    object HomeClicked : Event()
    object SearchClicked : Event()
    object ResetDataClicked : Event()
    object SettingsClicked : Event()

    data class ContentClicked(val content: HomeContent) : Event()
    data class ContentLongClicked(val content: HomeContent) : Event()
  }
}
