package net.lachlanmckee.bookmark.feature.home

import net.lachlanmckee.bookmark.feature.BookmarkViewModel
import net.lachlanmckee.bookmark.feature.home.model.HomeContent

internal interface HomeViewModel : BookmarkViewModel<HomeViewModel.State, HomeViewModel.Event> {

  sealed class State {
    object Empty : State()
    data class BookmarksExist(
      val contentList: List<HomeContent>,
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

    data class ContentClicked(val content: HomeContent) : Event()
    data class ContentLongClicked(val content: HomeContent) : Event()
  }
}
