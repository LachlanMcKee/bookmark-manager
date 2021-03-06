package net.lachlanmckee.bookmark.components.row

import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun RowText(text: AnnotatedString, style: TextStyle) {
  Text(
    text = text,
    style = style,
    color = LocalContentColor.current,
    overflow = TextOverflow.Ellipsis,
    maxLines = 1
  )
}
