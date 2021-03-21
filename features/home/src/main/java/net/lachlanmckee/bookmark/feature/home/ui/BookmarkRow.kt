package net.lachlanmckee.bookmark.feature.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import net.lachlanmckee.bookmark.components.chip.layout.ChipFlowRow
import net.lachlanmckee.bookmark.components.row.CheckableRow
import net.lachlanmckee.bookmark.feature.home.model.HomeContent
import net.lachlanmckee.bookmark.feature.ui.BookmarkRowContent

@Composable
internal fun BookmarkRow(
  label: AnnotatedString,
  link: AnnotatedString,
  metadata: List<HomeContent.BookmarkContent.Metadata>,
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
      CompositionLocalProvider(LocalContentColor provides Color.Black) {
        Column {
          BookmarkRowContent(
            label = label,
            link = link
          )

          ChipFlowRow(
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
