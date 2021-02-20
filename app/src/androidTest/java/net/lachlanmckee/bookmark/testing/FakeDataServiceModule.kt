package net.lachlanmckee.bookmark.testing

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import net.lachlanmckee.bookmark.service.persistence.BookmarkDatabase
import net.lachlanmckee.bookmark.service.persistence.ProdPersistenceModule
import javax.inject.Singleton

@Module
@TestInstallIn(
  components = [SingletonComponent::class],
  replaces = [ProdPersistenceModule::class]
)
object FakeDataServiceModule {
  @Singleton
  @Provides
  fun provideBookmarkDatabase(
    @ApplicationContext context: Context
  ): BookmarkDatabase {
    return Room
      .inMemoryDatabaseBuilder(context, BookmarkDatabase::class.java)
      .build()
  }
}
