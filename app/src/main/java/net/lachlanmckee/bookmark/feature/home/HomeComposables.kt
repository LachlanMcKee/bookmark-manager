package net.lachlanmckee.bookmark.feature.home

import androidx.compose.material.AmbientContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.graphics.Color
import net.lachlanmckee.bookmark.feature.CheckableRow
import net.lachlanmckee.bookmark.feature.RowText

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
