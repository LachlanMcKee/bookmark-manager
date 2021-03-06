package net.lachlanmckee.bookmark.components.chip.layout

import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import net.lachlanmckee.bookmark.components.chip.Chip

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
