package net.lachlanmckee.bookmark.feature.settings.di

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import net.lachlanmckee.bookmark.feature.NavigationDelegationNavFactory
import net.lachlanmckee.bookmark.feature.create
import net.lachlanmckee.bookmark.feature.settings.SettingsViewModelImpl
import net.lachlanmckee.bookmark.feature.settings.ui.SettingsScreen
import net.lachlanmckee.hilt.compose.navigation.factory.ComposeNavigationFactory
import net.lachlanmckee.hilt.compose.navigation.factory.HiltComposeNavigationFactory
import javax.inject.Inject

@HiltComposeNavigationFactory
internal class SettingsComposeNavigationFactory @Inject constructor(
  private val navigationDelegationNavFactory: NavigationDelegationNavFactory
) : ComposeNavigationFactory {
  override fun create(builder: NavGraphBuilder, navController: NavHostController) {
    navigationDelegationNavFactory.create<SettingsViewModelImpl>(
      builder = builder,
      navController = navController,
      route = "settings",
      content = {
        SettingsScreen(state, eventConsumer)
      }
    )
  }
}
