package net.lachlanmckee.bookmark.feature.settings.ui

import androidx.activity.compose.BackHandler
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kotlinx.coroutines.flow.StateFlow
import net.lachlanmckee.bookmark.feature.settings.SettingsViewModel.Event
import net.lachlanmckee.bookmark.feature.settings.SettingsViewModel.State
import net.lachlanmckee.bookmark.feature.ui.RootBottomAppBar
import timber.log.Timber

@Composable
internal fun SettingsScreen(
  stateFlow: StateFlow<State>,
  events: (Event) -> Unit
) {
  val state: State by stateFlow.collectAsState()
  Timber.d("SettingsScreen $state")

  BackHandler {
    events(Event.Back)
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(text = "Settings")
        }
      )
    },
    content = {
      when (state) {
        State.Empty -> {
          Text(text = "Settings screen")
        }
      }
    },
    bottomBar = {
      RootBottomAppBar(
        homeClick = { events(Event.HomeClicked) },
        searchClick = { events(Event.SearchClicked) },
        resetClick = {},
        settingsClick = { events(Event.SettingsClicked) }
      )
    }
  )
}
