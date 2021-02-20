package net.lachlanmckee.bookmark.compose

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment

fun Fragment.fragmentComposeView(
  func: @Composable () -> Unit
): ComposeView {
  return ComposeView(requireContext()).apply {
    setContent {
      MaterialTheme {
        func()
      }
    }
  }
}
