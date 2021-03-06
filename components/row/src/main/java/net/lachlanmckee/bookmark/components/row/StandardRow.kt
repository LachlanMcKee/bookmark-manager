package net.lachlanmckee.bookmark.components.row

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
