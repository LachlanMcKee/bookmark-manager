package net.lachlanmckee.bookmark.feature.home.di

import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import net.lachlanmckee.bookmark.feature.NavigationDelegationNavFactory
import net.lachlanmckee.bookmark.feature.create
import net.lachlanmckee.bookmark.feature.home.HomeViewModelImpl
import net.lachlanmckee.bookmark.feature.home.ui.HomeScreen
import net.lachlanmckee.hilt.compose.navigation.factory.ComposeNavigationFactory
import net.lachlanmckee.hilt.compose.navigation.factory.HiltComposeNavigationFactory
import javax.inject.Inject

@HiltComposeNavigationFactory
internal class HomeComposeNavigationFactory @Inject constructor(
  private val navigationDelegationNavFactory: NavigationDelegationNavFactory
) : ComposeNavigationFactory {
  override fun create(builder: NavGraphBuilder, navController: NavHostController) {
    navigationDelegationNavFactory.create<HomeViewModelImpl>(
      builder = builder,
      navController = navController,
      route = "home",
      content = {
        HomeScreen(
          state = state.collectAsState().value,
          events = eventConsumer
        )
      }
    )

    navigationDelegationNavFactory.create<HomeViewModelImpl>(
      builder = builder,
      navController = navController,
      route = "home/folder?folderId={folderId}",
      arguments = listOf(
        navArgument("folderId") {
          nullable = false
          type = NavType.StringType
        }
      ),
      content = {
        HomeScreen(
          state = state.collectAsState().value,
          events = eventConsumer
        )
      }
    )
  }
}
