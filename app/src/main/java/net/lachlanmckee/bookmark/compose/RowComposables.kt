package net.lachlanmckee.bookmark.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import net.lachlanmckee.bookmark.util.runIfNotNull

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StandardRow(
  backgroundColor: Color = Color.White,
  onClick: (() -> Unit)? = null,
  onLongClick: (() -> Unit)? = null,
  content: @Composable RowScope.() -> Unit
) {
  Surface(
    color = backgroundColor,
    modifier = Modifier
      .defaultMinSize(minHeight = 80.dp)
      .fillMaxWidth()
      .runIfNotNull(onClick) { nonNullOnClick ->
        combinedClickable(
          onClick = nonNullOnClick,
          onLongClick = onLongClick
        )
      }
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
fun CheckableRow(
  backgroundColor: Color,
  isSelected: Boolean,
  isInEditMode: Boolean,
  onClick: () -> Unit,
  onLongClick: (() -> Unit)? = null,
  content: @Composable RowScope.() -> Unit
) {
  StandardRow(
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
fun RowText(text: AnnotatedString, style: TextStyle) {
  Text(
    text = text,
    style = style,
    color = LocalContentColor.current,
    overflow = TextOverflow.Ellipsis,
    maxLines = 1
  )
}
