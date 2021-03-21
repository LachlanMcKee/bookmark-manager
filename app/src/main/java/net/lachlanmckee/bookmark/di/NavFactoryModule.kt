package net.lachlanmckee.bookmark.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.lachlanmckee.bookmark.NavigationDelegationNavFactoryImpl
import net.lachlanmckee.bookmark.feature.NavigationDelegationNavFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NavFactoryModule {
  @Singleton
  @Binds
  fun bindNavigationDelegationNavFactory(navFactory: NavigationDelegationNavFactoryImpl): NavigationDelegationNavFactory
}
