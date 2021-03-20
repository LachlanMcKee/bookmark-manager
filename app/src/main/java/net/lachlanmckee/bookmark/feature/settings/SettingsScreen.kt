package net.lachlanmckee.bookmark.feature.settings

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import net.lachlanmckee.bookmark.feature.RootBottomAppBar

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(text = "Settings")
        }
      )
    },
    content = {
      Text(text = "Settings screen")
    },
    bottomBar = {
      RootBottomAppBar(
        homeClick = { viewModel.homeClicked() },
        searchClick = { viewModel.searchClicked() },
        resetClick = { TODO() },
        settingsClick = { viewModel.settingsClicked() }
      )
    }
  )
}
