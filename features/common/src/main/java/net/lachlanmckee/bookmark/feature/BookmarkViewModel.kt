package net.lachlanmckee.bookmark.feature

import androidx.lifecycle.LiveData

interface BookmarkViewModel<STATE, EVENT> {
  val state: LiveData<STATE>
  val eventConsumer: (EVENT) -> Unit
  val navigation: LiveData<Navigation>
}
