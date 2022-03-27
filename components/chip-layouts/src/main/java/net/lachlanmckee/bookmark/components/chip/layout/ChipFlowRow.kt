package net.lachlanmckee.bookmark.components.chip.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> ChipFlowRow(
  modifier: Modifier = Modifier,
  style: TextStyle = MaterialTheme.typography.subtitle1,
  leadingIcon: @Composable (() -> Unit)? = null,
  data: List<T>,
  labelFunc: (T) -> AnnotatedString,
  onClick: (T) -> Unit
) {
  ChipFlowRowContainer(modifier) {
    data.forEach { item ->
      Chip(
        onClick = { onClick(item) },
        leadingIcon = leadingIcon,
        content = { Text(text = labelFunc(item), style = style) }
      )
    }
  }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChipFlowRowPlaceholder(
  modifier: Modifier = Modifier,
  chipWidth: Dp = 80.dp,
  count: Int,
) {
  ChipFlowRowContainer(modifier) {
    repeat(count) {
      Box(
        modifier = Modifier
          .width(chipWidth)
          .height(ChipDefaults.MinHeight)
          .placeholder(
            visible = true,
            shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
            highlight = PlaceholderHighlight.fade(),
          )
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
