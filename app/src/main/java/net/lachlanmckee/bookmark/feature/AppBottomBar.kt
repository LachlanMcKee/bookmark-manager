package net.lachlanmckee.bookmark.feature

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AppBottomBar(rootViewModel: RootViewModel) {
    BottomAppBar(cutoutShape = CircleShape) {
        IconButton(onClick = { rootViewModel.homeClicked() }) {
            Icon(Icons.Filled.Home)
        }
        Spacer(Modifier.weight(1f, true))
        IconButton(onClick = { rootViewModel.settingsClicked() }) {
            Icon(Icons.Filled.Settings)
        }
    }
}