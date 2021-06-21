package net.lachlanmckee.bookmark.components.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ScrollToTopLazyColumn(
  content: LazyListScope.() -> Unit
) {
  Box {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LazyColumn(
      state = listState,
      content = content
    )

    val showButton: Boolean by remember {
      derivedStateOf {
        listState.firstVisibleItemIndex > 0
      }
    }

    AnimatedVisibility(
      visible = showButton,
      enter = fadeIn(),
      exit = fadeOut(),
      modifier = Modifier.align(Alignment.BottomCenter).padding(8.dp),
      content = {
        IconButton(
          onClick = {
            scope.launch {
              listState.scrollToItem(0)
            }
          },
          modifier = Modifier.padding(4.dp).background(Color.Blue, CircleShape)
        ) {
          Icon(
            imageVector = Icons.Rounded.KeyboardArrowUp,
            tint = Color.White,
            contentDescription = "Scroll to top"
          )
        }
      }
    )
  }
}
