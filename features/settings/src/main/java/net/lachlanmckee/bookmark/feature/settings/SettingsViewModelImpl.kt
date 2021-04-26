package net.lachlanmckee.bookmark.feature.settings

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import net.lachlanmckee.bookmark.feature.StandardViewModel
import net.lachlanmckee.bookmark.feature.model.Navigation
import net.lachlanmckee.bookmark.feature.settings.SettingsViewModel.Event
import net.lachlanmckee.bookmark.feature.settings.SettingsViewModel.State
import javax.inject.Inject

@HiltViewModel
internal class SettingsViewModelImpl @Inject constructor() :
  StandardViewModel<State, Event>(), SettingsViewModel {

  override val initialState: State = State.Empty

  override fun createState(): Flow<State> = emptyFlow()

  override val eventConsumer: (Event) -> Unit = { event ->
    when (event) {
      Event.Back -> navigate(Navigation.Back)
      Event.HomeClicked -> navigate(Navigation.Home)
      Event.SearchClicked -> navigate(Navigation.Search)
      Event.SettingsClicked -> navigate(Navigation.Settings)
    }
  }
}
