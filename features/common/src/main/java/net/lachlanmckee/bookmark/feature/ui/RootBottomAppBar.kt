package net.lachlanmckee.bookmark.feature.ui

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag

@Composable
fun RootBottomAppBar(
  homeClick: () -> Unit,
  searchClick: () -> Unit,
  resetClick: () -> Unit,
  settingsClick: () -> Unit
) {
  BottomAppBar(cutoutShape = CircleShape) {
    BottomAppBarButton(
      contentDescription = "Home navigation",
      icon = Icons.Filled.Home,
      onClick = { homeClick() }
    )
    BottomAppBarButton(
      contentDescription = "Search navigation",
      icon = Icons.Filled.Search,
      onClick = { searchClick() }
    )
    Spacer(Modifier.weight(1f, true))
    BottomAppBarButton(
      contentDescription = "Refresh navigation",
      icon = Icons.Filled.Refresh,
      onClick = { resetClick() }
    )
    BottomAppBarButton(
      contentDescription = "Settings navigation",
      icon = Icons.Filled.Settings,
      onClick = { settingsClick() }
    )
  }
}

@Composable
private fun BottomAppBarButton(
  contentDescription: String,
  icon: ImageVector,
  onClick: () -> Unit
) {
  IconButton(
    // TODO: Compose appears to have broken searching by content description in beta04
    modifier = Modifier.testTag(contentDescription),
    onClick = onClick,
    content = { Icon(icon, contentDescription) }
  )
}
