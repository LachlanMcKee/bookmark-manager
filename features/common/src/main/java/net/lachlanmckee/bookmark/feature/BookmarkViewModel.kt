package net.lachlanmckee.bookmark.feature

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import net.lachlanmckee.bookmark.feature.model.Navigation

interface BookmarkViewModel<STATE, EVENT> {
  val state: LiveData<STATE>
  val eventConsumer: (EVENT) -> Unit
  val navigation: Flow<Navigation>
}
