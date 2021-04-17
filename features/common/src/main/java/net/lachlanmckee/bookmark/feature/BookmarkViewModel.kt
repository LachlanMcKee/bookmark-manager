package net.lachlanmckee.bookmark.feature

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import net.lachlanmckee.bookmark.feature.model.Navigation

interface BookmarkViewModel<STATE, EVENT> {
  val state: StateFlow<STATE>
  val eventConsumer: (EVENT) -> Unit
  val navigation: Flow<Navigation>
}
