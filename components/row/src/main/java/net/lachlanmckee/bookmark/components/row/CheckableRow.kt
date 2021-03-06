package net.lachlanmckee.bookmark.components.row

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
