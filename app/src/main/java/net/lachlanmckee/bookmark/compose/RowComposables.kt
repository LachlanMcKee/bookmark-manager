package net.lachlanmckee.bookmark.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.AmbientContentColor
import androidx.compose.material.Checkbox
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun ClickableRow(
  backgroundColor: Color = Color.White,
  onClick: () -> Unit,
  onLongClick: () -> Unit = {},
  content: @Composable RowScope.() -> Unit
) {
  Surface(
    color = backgroundColor,
    modifier = Modifier
      .defaultMinSizeConstraints(minHeight = 80.dp)
      .fillMaxWidth()
      .clickable(
        onClick = onClick,
        onLongClick = onLongClick
      )
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
  onLongClick: () -> Unit,
  content: @Composable RowScope.() -> Unit
) {
  ClickableRow(
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
fun RowText(text: String, style: TextStyle) {
  Text(
    text = text,
    style = style,
    color = AmbientContentColor.current,
    overflow = TextOverflow.Ellipsis,
    maxLines = 1
  )
}
