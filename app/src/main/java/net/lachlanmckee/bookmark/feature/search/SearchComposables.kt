package net.lachlanmckee.bookmark.feature.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayout
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AmbientContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@ExperimentalLayout
@Composable
fun ChipCollection(
  modifier: Modifier,
  labels: List<String>,
  style: TextStyle = MaterialTheme.typography.subtitle1
) {
  Box(modifier = modifier) {
    FlowRow(
      mainAxisSpacing = 4.dp,
      crossAxisSpacing = 4.dp,
      content = {
        labels.forEach { label ->
          Chip(text = label, style = style)
        }
      }
    )
  }
}

@Composable
fun Chip(
  text: String,
  style: TextStyle
) {
    Text(
      modifier = Modifier
        .clickable(
          onClick = {}
        )
        .background(Color.LightGray, RoundedCornerShape(8.dp))
        .padding(8.dp),
      text = text,
      style = style,
      color = AmbientContentColor.current,
      overflow = TextOverflow.Ellipsis,
      maxLines = 1
    )
}
