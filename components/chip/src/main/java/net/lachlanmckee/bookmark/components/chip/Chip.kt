package net.lachlanmckee.bookmark.components.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.lachlanmckee.bookmark.util.runIfNotNull

@Composable
fun Chip(
  modifier: Modifier = Modifier,
  text: AnnotatedString,
  backgroundColor: Color = Color.Unspecified,
  style: TextStyle = MaterialTheme.typography.subtitle1,
  showCloseIcon: Boolean = false,
  onClick: (() -> Unit)? = null
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
    modifier = modifier
      .runIfNotNull(onClick) { nonNullOnClick ->
        clickable(onClick = nonNullOnClick)
      }
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
