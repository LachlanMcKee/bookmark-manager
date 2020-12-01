package net.lachlanmckee.bookmark.service.repository

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
  fun provideBookmarkRepository(impl: BookmarkRepositoryImpl): BookmarkRepository = impl

  @Singleton
  @Provides
  fun provideFolderRepository(impl: FolderRepositoryImpl): FolderRepository = impl
}
