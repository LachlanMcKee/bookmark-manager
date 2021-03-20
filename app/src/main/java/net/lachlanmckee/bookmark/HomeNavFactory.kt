package net.lachlanmckee.bookmark

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import net.lachlanmckee.bookmark.feature.home.HomeScreen
import net.lachlanmckee.bookmark.feature.home.HomeViewModelImpl
import javax.inject.Inject

class HomeNavFactory @Inject constructor(
  private val navigationDelegationNavFactory: NavigationDelegationNavFactory
) : NavFactory {
  override fun create(builder: NavGraphBuilder, navController: NavHostController) {
    navigationDelegationNavFactory.create<HomeViewModelImpl>(
      builder = builder,
      navController = navController,
      route = "home",
      content = {
        HomeScreen(
          stateLiveData = state,
          events = eventConsumer
        )
      }
    )
  }
}
