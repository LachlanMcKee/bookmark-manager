package net.lachlanmckee.bookmark.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@ExperimentalLayout
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

@Composable
fun <T> ChipHorizontalList(
  modifier: Modifier,
  style: TextStyle = MaterialTheme.typography.subtitle1,
  showCloseIcon: Boolean = false,
  data: List<T>,
  isSelected: List<Boolean>,
  labelFunc: (T) -> AnnotatedString,
  onClick: (T) -> Unit
) {
  LazyRow(
    modifier = modifier,
    contentPadding = PaddingValues(8.dp),
    horizontalArrangement = spacedBy(8.dp)
  ) {
    itemsIndexed(data) { index, item ->
      Chip(
        text = labelFunc(item),
        backgroundColor = if (isSelected[index]) {
          Color.Red
        } else {
          Color.Unspecified
        },
        style = style,
        showCloseIcon = showCloseIcon,
        onClick = { onClick(item) }
      )
    }
  }
}

@Composable
fun Chip(
  text: AnnotatedString,
  backgroundColor: Color = Color.Unspecified,
  style: TextStyle = MaterialTheme.typography.subtitle1,
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
          Icon(Icons.Rounded.Cancel, "Remove")
        }
      )
    )
  } else {
    emptyMap()
  }

  Text(
    modifier = Modifier
      .clickable(onClick = onClick)
      .background(backgroundColor.takeOrElse { Color.LightGray }, RoundedCornerShape(8.dp))
      .padding(vertical = 8.dp, horizontal = 12.dp),
    text = chipText,
    style = style,
    color = LocalContentColor.current,
    overflow = TextOverflow.Ellipsis,
    maxLines = 1,
    inlineContent = inlineTextContent
  )
}
