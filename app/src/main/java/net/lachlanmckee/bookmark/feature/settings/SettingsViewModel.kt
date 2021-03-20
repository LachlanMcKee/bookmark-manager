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
class SettingsViewModel @Inject constructor() : ViewModel(),
  net.lachlanmckee.bookmark.feature.BookmarkViewModel<Nothing, Nothing> {

  private val navigationFlow = MutableLiveData<net.lachlanmckee.bookmark.feature.Navigation>()

  fun homeClicked() {
    navigationFlow.value = net.lachlanmckee.bookmark.feature.Navigation.Home
  }

  fun searchClicked() {
    navigationFlow.value = net.lachlanmckee.bookmark.feature.Navigation.Search
  }

  fun settingsClicked() {
    navigationFlow.value = net.lachlanmckee.bookmark.feature.Navigation.Settings
  }

  override val state: LiveData<Nothing>
    get() = liveData { }

  override val eventConsumer: (Nothing) -> Unit = {}

  override val navigation: LiveData<net.lachlanmckee.bookmark.feature.Navigation>
    get() = navigationFlow
}
