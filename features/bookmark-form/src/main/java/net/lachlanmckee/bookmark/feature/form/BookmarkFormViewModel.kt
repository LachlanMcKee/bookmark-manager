package net.lachlanmckee.bookmark.feature.form

import net.lachlanmckee.bookmark.feature.BookmarkViewModel
import net.lachlanmckee.bookmark.feature.form.BookmarkFormViewModel.Event
import net.lachlanmckee.bookmark.feature.form.BookmarkFormViewModel.State

internal interface BookmarkFormViewModel : BookmarkViewModel<State, Event> {

  data class State(
    val name: String,
    val url: String,
  ) {
    companion object {
      val emptyState = State(
        name = "",
        url = "",
      )
    }
  }

  sealed class Event {
    object Back : Event()
    object Save : Event()
    data class NameUpdated(val name: String) : Event()
    data class UrlUpdated(val url: String) : Event()
  }
}
