package net.lachlanmckee.bookmark.compose

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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@ExperimentalLayout
@Composable
fun <T> ChipCollection(
  modifier: Modifier,
  style: TextStyle = MaterialTheme.typography.subtitle1,
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
            onClick = { onClick(item) }
          )
        }
      }
    )
  }
}

@Composable
fun Chip(
  text: AnnotatedString,
  style: TextStyle,
  onClick: () -> Unit
) {
  Text(
    modifier = Modifier
      .clickable(onClick = onClick)
      .background(Color.LightGray, RoundedCornerShape(8.dp))
      .padding(8.dp),
    text = text,
    style = style,
    color = AmbientContentColor.current,
    overflow = TextOverflow.Ellipsis,
    maxLines = 1
  )
}
