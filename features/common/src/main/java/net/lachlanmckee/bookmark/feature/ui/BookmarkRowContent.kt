package net.lachlanmckee.bookmark.feature.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder

@Composable
fun BookmarkRowContent(
  label: AnnotatedString,
  link: AnnotatedString
) {
  Column {
    TitleText(label = label)
    LinkText(label = link)
  }
}

@Composable
fun BookmarkRowContentPreview() {
  Column {
    TitleText(
      modifier = Modifier
        .width(200.dp)
        .placeholder(
          visible = true,
          highlight = PlaceholderHighlight.fade()
        ),
      label = AnnotatedString("")
    )
    LinkText(
      modifier = Modifier
        .width(400.dp)
        .placeholder(
          visible = true,
          highlight = PlaceholderHighlight.fade()
        ),
      label = AnnotatedString("")
    )
  }
}

@Composable
private fun TitleText(
  modifier: Modifier = Modifier,
  label: AnnotatedString
) {
  Text(
    modifier = modifier,
    text = label,
    style = MaterialTheme.typography.h6,
    color = Color.Black,
    overflow = TextOverflow.Ellipsis,
    maxLines = 1
  )
}

@Composable
private fun LinkText(
  modifier: Modifier = Modifier,
  label: AnnotatedString
) {
  Text(
    modifier = Modifier.padding(top = 4.dp).then(modifier),
    text = label,
    style = MaterialTheme.typography.subtitle1,
    color = Color.Black,
    overflow = TextOverflow.Ellipsis,
    maxLines = 1
  )
}
