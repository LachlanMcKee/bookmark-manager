package net.lachlanmckee.bookmark

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NavFactoryModule {
  @Singleton
  @Binds
  fun bindNavigationDelegationNavFactory(navFactory: NavigationDelegationNavFactoryImpl): NavigationDelegationNavFactory

  @Singleton
  @Binds
  @IntoSet
  fun bindHomeNavFactory(navFactory: HomeNavFactory): NavFactory
}
