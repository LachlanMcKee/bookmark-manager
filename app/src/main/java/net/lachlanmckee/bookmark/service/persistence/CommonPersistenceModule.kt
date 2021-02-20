package net.lachlanmckee.bookmark.service.persistence

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.lachlanmckee.bookmark.service.persistence.dao.BookmarkDao
import net.lachlanmckee.bookmark.service.persistence.dao.FolderDao
import net.lachlanmckee.bookmark.service.persistence.dao.MetadataDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object CommonPersistenceModule {
  @Singleton
  @Provides
  fun provideBookmarkDao(database: BookmarkDatabase): BookmarkDao {
    return database.bookmarkDao()
  }

  @Singleton
  @Provides
  fun provideFolderDao(database: BookmarkDatabase): FolderDao {
    return database.folderDao()
  }

  @Singleton
  @Provides
  fun provideMetadataDao(database: BookmarkDatabase): MetadataDao {
    return database.metadataDao()
  }
}
