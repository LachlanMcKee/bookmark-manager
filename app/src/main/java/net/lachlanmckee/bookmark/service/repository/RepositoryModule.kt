package net.lachlanmckee.bookmark.service.repository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
internal object RepositoryModule {
    @Provides
    fun provideBookmarkRepository(impl: BookmarkRepositoryImpl): BookmarkRepository = impl
}
