package net.lachlanmckee.bookmark.compose

import androidx.compose.runtime.Composable

fun ConditionalComposable(
  condition: Boolean,
  composable: @Composable () -> Unit
): @Composable (() -> Unit)? {
  return if (condition) composable else null
}
