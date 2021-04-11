package net.lachlanmckee.bookmark.feature.settings

import net.lachlanmckee.bookmark.feature.BookmarkViewModel
import net.lachlanmckee.bookmark.feature.settings.SettingsViewModel.Event
import net.lachlanmckee.bookmark.feature.settings.SettingsViewModel.State

internal interface SettingsViewModel : BookmarkViewModel<State, Event> {
  sealed class State {
    object Empty : State()
  }

  sealed class Event {
    object Back : Event()
    object HomeClicked : Event()
    object SearchClicked : Event()
    object SettingsClicked : Event()
  }
}
