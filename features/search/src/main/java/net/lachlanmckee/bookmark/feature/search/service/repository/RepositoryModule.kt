package net.lachlanmckee.bookmark.feature.search.service.repository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object RepositoryModule {
  @Singleton
  @Provides
  fun provideBookmarkRepository(impl: SearchRepositoryImpl): SearchRepository = impl
}
