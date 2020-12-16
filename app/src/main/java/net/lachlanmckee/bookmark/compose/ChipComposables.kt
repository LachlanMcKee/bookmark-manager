package net.lachlanmckee.bookmark.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayout
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.AmbientContentColor
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@ExperimentalLayout
@Composable
fun <T> ChipCollection(
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

@Composable
fun Chip(
  text: AnnotatedString,
  style: TextStyle,
  showCloseIcon: Boolean = false,
  onClick: () -> Unit
) {
  val chipText = if (showCloseIcon) {
    buildAnnotatedString {
      append(text)
      append(" ")
      appendInlineContent("cross")
    }
  } else {
    text
  }

  val inlineTextContent: Map<String, InlineTextContent> = if (showCloseIcon) {
    mapOf(
      "cross" to InlineTextContent(
        placeholder = Placeholder(
          width = 20.sp,
          height = 20.sp,
          placeholderVerticalAlign = PlaceholderVerticalAlign.Center
        ),
        children = {
          Icon(Icons.Rounded.Cancel)
        }
      )
    )
  } else {
    emptyMap()
  }

  Text(
    modifier = Modifier
      .clickable(onClick = onClick)
      .background(Color.LightGray, RoundedCornerShape(8.dp))
      .padding(vertical = 8.dp, horizontal = 12.dp),
    text = chipText,
    style = style,
    color = AmbientContentColor.current,
    overflow = TextOverflow.Ellipsis,
    maxLines = 1,
    inlineContent = inlineTextContent
  )
}
