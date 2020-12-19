package net.lachlanmckee.bookmark.feature.home

import androidx.compose.material.AmbientContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import net.lachlanmckee.bookmark.compose.CheckableRow
import net.lachlanmckee.bookmark.compose.RowText
import net.lachlanmckee.bookmark.feature.BookmarkRowContent

@Composable
fun BookmarkRow(
  label: AnnotatedString,
  link: AnnotatedString,
  isSelected: Boolean = false,
  isInEditMode: Boolean = false,
  onClick: () -> Unit,
  onLongClick: (() -> Unit)? = null
) {
  CheckableRow(
    backgroundColor = Color.White,
    isSelected = isSelected,
    isInEditMode = isInEditMode,
    onClick = onClick,
    onLongClick = onLongClick,
    content = {
      Providers(AmbientContentColor provides Color.Black) {
        BookmarkRowContent(
          label = label,
          link = link
        )
      }
    }
  )
}

@Composable
fun FolderRow(
  label: AnnotatedString,
  isSelected: Boolean,
  isInEditMode: Boolean,
  onClick: () -> Unit,
  onLongClick: (() -> Unit)? = null
) {
  CheckableRow(
    backgroundColor = Color.Gray,
    isSelected = isSelected,
    isInEditMode = isInEditMode,
    onClick = onClick,
    onLongClick = onLongClick,
    content = {
      Providers(AmbientContentColor provides Color.White) {
        RowText(
          text = label,
          style = MaterialTheme.typography.h6
        )
      }
    }
  )
}
