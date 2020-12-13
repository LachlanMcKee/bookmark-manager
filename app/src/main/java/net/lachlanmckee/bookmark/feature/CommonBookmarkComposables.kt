package net.lachlanmckee.bookmark.feature

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import net.lachlanmckee.bookmark.compose.RowText

@Composable
fun BookmarkRowContent(
  label: String,
  link: String,
) {
  Column {
    RowText(text = label, style = MaterialTheme.typography.h6)
    RowText(text = link, style = MaterialTheme.typography.subtitle1)
  }
}
