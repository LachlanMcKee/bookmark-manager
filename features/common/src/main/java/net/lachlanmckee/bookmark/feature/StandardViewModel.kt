package net.lachlanmckee.bookmark.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import net.lachlanmckee.bookmark.feature.model.Navigation

abstract class StandardViewModel<STATE, EVENT> : ViewModel(), BookmarkViewModel<STATE, EVENT> {
  private val navigationSharedFlow = MutableSharedFlow<Navigation>()

  abstract val initialState: STATE
  abstract fun createState(): Flow<STATE>

  final override val state: StateFlow<STATE> by lazy {
    createState()
      .distinctUntilChanged()
      .stateIn(viewModelScope, SharingStarted.Eagerly, initialState)
  }

  final override val navigation: Flow<Navigation>
    get() = navigationSharedFlow

  fun navigate(navigation: Navigation) {
    viewModelScope.launch {
      navigationSharedFlow.emit(navigation)
    }
  }
}
