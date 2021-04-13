package net.lachlanmckee.bookmark.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import net.lachlanmckee.bookmark.feature.model.Navigation
import net.lachlanmckee.bookmark.feature.settings.SettingsViewModel.Event
import net.lachlanmckee.bookmark.feature.settings.SettingsViewModel.State
import javax.inject.Inject

@HiltViewModel
internal class SettingsViewModelImpl @Inject constructor() :
  ViewModel(), SettingsViewModel {

  private val navigationSharedFlow = MutableSharedFlow<Navigation>()

  override val state: StateFlow<State>
    get() = MutableStateFlow(State.Empty)

  override val eventConsumer: (Event) -> Unit = { event ->
    viewModelScope.launch {
      when (event) {
        Event.Back -> navigationSharedFlow.emit(Navigation.Back)
        Event.HomeClicked -> navigationSharedFlow.emit(Navigation.Home)
        Event.SearchClicked -> navigationSharedFlow.emit(Navigation.Search)
        Event.SettingsClicked -> navigationSharedFlow.emit(Navigation.Settings)
      }
    }
  }

  override val navigation: Flow<Navigation>
    get() = navigationSharedFlow
}
