package net.lachlanmckee.bookmark.feature.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import net.lachlanmckee.bookmark.feature.BookmarkViewModel
import net.lachlanmckee.bookmark.feature.Navigation
import javax.inject.Inject

@HiltViewModel
internal class SettingsViewModel @Inject constructor() :
  ViewModel(),
  BookmarkViewModel<Nothing, Nothing> {

  private val navigationFlow = MutableLiveData<Navigation>()

  fun homeClicked() {
    navigationFlow.value = Navigation.Home
  }

  fun searchClicked() {
    navigationFlow.value = Navigation.Search
  }

  fun settingsClicked() {
    navigationFlow.value = Navigation.Settings
  }

  override val state: LiveData<Nothing>
    get() = liveData { }

  override val eventConsumer: (Nothing) -> Unit = {}

  override val navigation: LiveData<Navigation>
    get() = navigationFlow
}
