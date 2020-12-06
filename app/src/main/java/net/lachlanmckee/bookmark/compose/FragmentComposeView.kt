package net.lachlanmckee.bookmark.compose

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData

fun <T> Fragment.fragmentComposeView(
  state: LiveData<T>,
  func: @Composable (T) -> Unit
): ComposeView {
  return ComposeView(requireContext()).apply {
    setContent {
      MaterialTheme {
        state.observeAsState().value.let { state ->
          if (state != null) {
            func(state)
          }
        }
      }
    }
  }
}
