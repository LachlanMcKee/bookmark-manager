package net.lachlanmckee.bookmark.components.chip.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import net.lachlanmckee.bookmark.components.chip.Chip

@Composable
fun <T> ChipFlowRow(
  modifier: Modifier,
  style: TextStyle = MaterialTheme.typography.subtitle1,
  showCloseIcon: Boolean = false,
  data: List<T>,
  labelFunc: (T) -> AnnotatedString,
  onClick: (T) -> Unit
) {
  Box(modifier = modifier) {
    FlowRow(
      mainAxisSpacing = 4.dp,
      crossAxisSpacing = 4.dp,
      content = {
        data.forEach { item ->
          Chip(
            text = labelFunc(item),
            style = style,
            showCloseIcon = showCloseIcon,
            onClick = { onClick(item) }
          )
        }
      }
    )
  }
}
