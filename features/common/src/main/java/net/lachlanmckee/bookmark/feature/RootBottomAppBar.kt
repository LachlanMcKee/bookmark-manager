package net.lachlanmckee.bookmark.feature

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RootBottomAppBar(
  homeClick: () -> Unit,
  searchClick: () -> Unit,
  resetClick: () -> Unit,
  settingsClick: () -> Unit
) {
  BottomAppBar(cutoutShape = CircleShape) {
    IconButton(onClick = { homeClick() }) {
      Icon(Icons.Filled.Home, "Home")
    }
    IconButton(onClick = { searchClick() }) {
      Icon(Icons.Filled.Search, "Search")
    }
    Spacer(Modifier.weight(1f, true))
    IconButton(onClick = { resetClick() }) {
      Icon(Icons.Filled.Refresh, "Refresh")
    }
    IconButton(onClick = { settingsClick() }) {
      Icon(Icons.Filled.Settings, "Settings")
    }
  }
}
