package net.lachlanmckee.bookmark.feature.home

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import net.lachlanmckee.bookmark.feature.NavFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NavFactoryModule {
  @Singleton
  @Binds
  @IntoSet
  fun bindHomeNavFactory(navFactory: HomeNavFactory): NavFactory
}
