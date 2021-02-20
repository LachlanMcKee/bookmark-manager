package net.lachlanmckee.bookmark.service.persistence

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ProdPersistenceModule {
  @Singleton
  @Provides
  fun provideBookmarkDatabase(
    @ApplicationContext context: Context
  ): BookmarkDatabase {
    return Room
      .databaseBuilder(
        context,
        BookmarkDatabase::class.java,
        "bookmark-database"
      )
      .build()
  }
}
