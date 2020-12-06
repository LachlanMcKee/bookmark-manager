package net.lachlanmckee.bookmark.feature.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
private fun ClickableRow(
  backgroundColor: Color,
  onClick: () -> Unit,
  onLongClick: () -> Unit,
  content: @Composable RowScope.() -> Unit
) {
  Surface(
    color = backgroundColor,
    modifier = Modifier
      .defaultMinSizeConstraints(minHeight = 80.dp)
      .fillMaxWidth()
      .clickable(
        onClick = onClick,
        onLongClick = onLongClick
      )
  ) {
    Row(
      modifier = Modifier.padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      content()
    }
  }
}

@Composable
private fun CheckableRow(
  backgroundColor: Color,
  isSelected: Boolean,
  isInEditMode: Boolean,
  onClick: () -> Unit,
  onLongClick: () -> Unit,
  content: @Composable RowScope.() -> Unit
) {
  ClickableRow(
    backgroundColor = backgroundColor,
    onClick = onClick,
    onLongClick = onLongClick,
    content = {
      if (isInEditMode) {
        Checkbox(
          checked = isSelected,
          onCheckedChange = {},
          modifier = Modifier.padding(PaddingValues(end = 16.dp))
        )
      }
      content()
    }
  )
}

@Composable
fun RootBottomAppBar(
  homeClick: () -> Unit,
  resetClick: () -> Unit,
  settingsClick: () -> Unit
) {
  BottomAppBar(cutoutShape = CircleShape) {
    IconButton(onClick = { homeClick() }) {
      Icon(Icons.Filled.Home)
    }
    IconButton(onClick = { resetClick() }) {
      Icon(Icons.Filled.Refresh)
    }
    Spacer(Modifier.weight(1f, true))
    IconButton(onClick = { settingsClick() }) {
      Icon(Icons.Filled.Settings)
    }
  }
}

@Composable
fun FolderRow(
  label: String,
  isSelected: Boolean,
  isInEditMode: Boolean,
  onClick: () -> Unit,
  onLongClick: () -> Unit
) {
  CheckableRow(
    backgroundColor = Color.Gray,
    isSelected = isSelected,
    isInEditMode = isInEditMode,
    onClick = onClick,
    onLongClick = onLongClick,
    content = {
      Providers(AmbientContentColor provides Color.White) {
        RowText(text = label, style = MaterialTheme.typography.h6)
      }
    }
  )
}

@Composable
fun BookmarkRow(
  label: String,
  link: String,
  isSelected: Boolean,
  isInEditMode: Boolean,
  onClick: () -> Unit,
  onLongClick: () -> Unit
) {
  CheckableRow(
    backgroundColor = Color.White,
    isSelected = isSelected,
    isInEditMode = isInEditMode,
    onClick = onClick,
    onLongClick = onLongClick,
    content = {
      Providers(AmbientContentColor provides Color.Black) {
        Column {
          RowText(text = label, style = MaterialTheme.typography.h6)
          RowText(text = link, style = MaterialTheme.typography.subtitle1)
        }
      }
    }
  )
}

@Composable
private fun RowText(text: String, style: TextStyle) {
  Text(
    text = text,
    style = style,
    color = AmbientContentColor.current,
    overflow = TextOverflow.Ellipsis,
    maxLines = 1
  )
}
