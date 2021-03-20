package net.lachlanmckee.bookmark.feature.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import net.lachlanmckee.bookmark.feature.NavFactory
import net.lachlanmckee.bookmark.feature.NavigationDelegationNavFactory
import net.lachlanmckee.bookmark.feature.create
import javax.inject.Inject
import javax.inject.Singleton

internal class SettingsNavFactory @Inject constructor(
  private val navigationDelegationNavFactory: NavigationDelegationNavFactory
) : NavFactory {
  override fun create(builder: NavGraphBuilder, navController: NavHostController) {
    navigationDelegationNavFactory.create<SettingsViewModel>(
      builder = builder,
      navController = navController,
      route = "settings",
      content = {
        SettingsScreen(this)
      }
    )
  }
}

@Module
@InstallIn(SingletonComponent::class)
internal interface NavFactoryModule {
  @Singleton
  @Binds
  @IntoSet
  fun bindNavFactory(navFactory: SettingsNavFactory): NavFactory
}
