package net.lachlanmckee.bookmark.feature

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import net.lachlanmckee.bookmark.components.row.RowText

@Composable
fun BookmarkRowContent(
  label: AnnotatedString,
  link: AnnotatedString,
) {
  Column {
    RowText(text = label, style = MaterialTheme.typography.h6)
    RowText(text = link, style = MaterialTheme.typography.subtitle1)
  }
}
