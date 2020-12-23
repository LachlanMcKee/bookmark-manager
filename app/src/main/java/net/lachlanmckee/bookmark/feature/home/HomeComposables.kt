package net.lachlanmckee.bookmark.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayout
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AmbientContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import net.lachlanmckee.bookmark.compose.CheckableRow
import net.lachlanmckee.bookmark.compose.ChipCollection
import net.lachlanmckee.bookmark.compose.RowText
import net.lachlanmckee.bookmark.feature.BookmarkRowContent
import net.lachlanmckee.bookmark.feature.home.HomeViewModel.Content.BookmarkContent.Metadata

@ExperimentalLayout
@Composable
fun BookmarkRow(
  label: AnnotatedString,
  link: AnnotatedString,
  metadata: List<Metadata>,
  isSelected: Boolean = false,
  isInEditMode: Boolean = false,
  bookmarkOnClick: () -> Unit,
  bookmarkOnLongClick: () -> Unit,
  metadataOnClick: (Long) -> Unit
) {
  CheckableRow(
    backgroundColor = Color.White,
    isSelected = isSelected,
    isInEditMode = isInEditMode,
    onClick = bookmarkOnClick,
    onLongClick = bookmarkOnLongClick,
    content = {
      Providers(AmbientContentColor provides Color.Black) {
        Column {
          BookmarkRowContent(
            label = label,
            link = link
          )

          ChipCollection(
            modifier = Modifier.padding(top = 8.dp),
            data = metadata,
            labelFunc = { AnnotatedString(it.name) },
            onClick = { metadataOnClick(it.id) }
          )
        }
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
        RowText(
          text = label,
          style = MaterialTheme.typography.h6
        )
      }
    }
  )
}
