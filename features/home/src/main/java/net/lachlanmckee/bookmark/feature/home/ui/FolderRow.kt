package net.lachlanmckee.bookmark.feature.home.ui

import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import net.lachlanmckee.bookmark.components.row.CheckableRow
import net.lachlanmckee.bookmark.components.row.RowText

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
      CompositionLocalProvider(LocalContentColor provides Color.White) {
        RowText(
          text = label,
          style = MaterialTheme.typography.h6
        )
      }
    }
  )
}
