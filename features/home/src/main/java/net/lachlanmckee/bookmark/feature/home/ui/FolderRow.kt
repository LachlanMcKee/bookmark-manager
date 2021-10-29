package net.lachlanmckee.bookmark.feature.home.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import net.lachlanmckee.bookmark.components.row.CheckableRow

@Composable
internal fun FolderRow(
  label: AnnotatedString,
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
      Text(
        text = label,
        style = MaterialTheme.typography.h6,
        color = Color.White,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
      )
    }
  )
}
