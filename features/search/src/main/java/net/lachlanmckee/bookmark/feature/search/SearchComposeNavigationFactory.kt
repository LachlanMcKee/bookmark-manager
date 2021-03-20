package net.lachlanmckee.bookmark.feature.search

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import net.lachlanmckee.bookmark.feature.NavigationDelegationNavFactory
import net.lachlanmckee.bookmark.feature.create
import net.lachlanmckee.compose.navigation.ComposeNavigationFactory
import javax.inject.Inject
import javax.inject.Singleton

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
          stateLiveData = state,
          events = eventConsumer
        )
      }
    )
  }
}

@Module
@InstallIn(SingletonComponent::class)
internal interface ComposeNavigationFactoryModule {
  @Singleton
  @Binds
  @IntoSet
  fun bindComposeNavigationFactory(factory: SearchComposeNavigationFactory): ComposeNavigationFactory
}
