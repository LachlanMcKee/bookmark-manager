package net.lachlanmckee.bookmark.components.chip.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder
import net.lachlanmckee.bookmark.components.chip.Chip

@Composable
fun <T> ChipFlowRow(
  modifier: Modifier = Modifier,
  style: TextStyle = MaterialTheme.typography.subtitle1,
  showCloseIcon: Boolean = false,
  data: List<T>,
  labelFunc: (T) -> AnnotatedString,
  onClick: (T) -> Unit
) {
  ChipFlowRowContainer(modifier) {
    data.forEach { item ->
      Chip(
        text = labelFunc(item),
        style = style,
        showCloseIcon = showCloseIcon,
        onClick = { onClick(item) }
      )
    }
  }
}

@Composable
fun ChipFlowRowPlaceholder(
  modifier: Modifier = Modifier,
  style: TextStyle = MaterialTheme.typography.subtitle1,
  chipWidth: Dp = 80.dp,
  count: Int,
) {
  ChipFlowRowContainer(modifier) {
    repeat(count) {
      Chip(
        modifier = Modifier
          .width(chipWidth)
          .placeholder(
            visible = true,
            highlight = PlaceholderHighlight.fade(),
          ),
        text = AnnotatedString(""),
        style = style
      )
    }
  }
}

@Composable
private fun ChipFlowRowContainer(
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit
) {
  Box(modifier = modifier) {
    FlowRow(
      mainAxisSpacing = 4.dp,
      crossAxisSpacing = 4.dp,
      content = content
    )
  }
}
