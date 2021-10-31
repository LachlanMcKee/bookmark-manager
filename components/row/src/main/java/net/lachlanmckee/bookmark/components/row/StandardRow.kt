package net.lachlanmckee.bookmark.components.row

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.lachlanmckee.bookmark.util.runIfNotNull

@Composable
fun StandardRow(
  modifier: Modifier = Modifier,
  backgroundColor: Color = Color.White,
  onClick: (() -> Unit)? = null,
  onLongClick: (() -> Unit)? = null,
  content: @Composable RowScope.() -> Unit
) {
  Surface(
    color = backgroundColor,
    modifier = modifier
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
