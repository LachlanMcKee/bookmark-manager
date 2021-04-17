package net.lachlanmckee.bookmark.feature.search.di

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import net.lachlanmckee.bookmark.feature.NavigationDelegationNavFactory
import net.lachlanmckee.bookmark.feature.create
import net.lachlanmckee.bookmark.feature.search.SearchViewModelImpl
import net.lachlanmckee.bookmark.feature.search.ui.SearchScreen
import net.lachlanmckee.hilt.compose.navigation.factory.ComposeNavigationFactory
import net.lachlanmckee.hilt.compose.navigation.factory.HiltComposeNavigationFactory
import javax.inject.Inject

@HiltComposeNavigationFactory
internal class SearchComposeNavigationFactory @Inject constructor(
  private val navigationDelegationNavFactory: NavigationDelegationNavFactory
) : ComposeNavigationFactory {
  override fun create(builder: NavGraphBuilder, navController: NavHostController) {
    navigationDelegationNavFactory.create<SearchViewModelImpl>(
      builder = builder,
      navController = navController,
      route = "search",
      content = {
        SearchScreen(
          stateFlow = state,
          events = eventConsumer
        )
      }
    )
  }
}
