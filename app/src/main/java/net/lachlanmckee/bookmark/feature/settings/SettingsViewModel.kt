package net.lachlanmckee.bookmark.feature.settings

import androidx.lifecycle.ViewModel
import net.lachlanmckee.bookmark.feature.Navigator
import net.lachlanmckee.bookmark.feature.RootViewModel
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
  private val navigator: Navigator
) : ViewModel(), RootViewModel {

  override fun homeClicked() {
    navigator.home()
  }

  override fun settingsClicked() {
    navigator.settings()
  }
}
