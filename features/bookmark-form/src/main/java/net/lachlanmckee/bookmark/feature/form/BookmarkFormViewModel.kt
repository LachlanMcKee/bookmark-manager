package net.lachlanmckee.bookmark.feature.form

import net.lachlanmckee.bookmark.feature.BookmarkViewModel
import net.lachlanmckee.bookmark.feature.form.BookmarkFormViewModel.Event
import net.lachlanmckee.bookmark.feature.form.BookmarkFormViewModel.State

internal interface BookmarkFormViewModel : BookmarkViewModel<State, Event> {

  sealed class State {
    object Empty : State()
  }

  sealed class Event {
    object Back : Event()
    object Save : Event()
  }
}
