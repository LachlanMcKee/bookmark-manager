package net.lachlanmckee.bookmark.feature.home

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
  fun bindComposeNavigationFactory(factory: HomeComposeNavigationFactory): ComposeNavigationFactory
}
