package net.lachlanmckee.bookmark.service.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import net.lachlanmckee.bookmark.feature.search.service.persistence.SearchDatabase
import net.lachlanmckee.bookmark.service.persistence.dao.BookmarkDao
import net.lachlanmckee.bookmark.service.persistence.dao.FolderDao
import net.lachlanmckee.bookmark.service.persistence.dao.MetadataDao
import net.lachlanmckee.bookmark.service.persistence.entity.BookmarkEntity
import net.lachlanmckee.bookmark.service.persistence.entity.BookmarkMetadataCrossRef
import net.lachlanmckee.bookmark.service.persistence.entity.FolderEntity
import net.lachlanmckee.bookmark.service.persistence.entity.MetadataEntity

@Database(
  version = 1,
  entities = [
    BookmarkEntity::class,
    FolderEntity::class,
    MetadataEntity::class,
    BookmarkMetadataCrossRef::class
  ]
)
abstract class BookmarkDatabase : RoomDatabase(), SearchDatabase {
  abstract fun bookmarkDao(): BookmarkDao
  abstract fun folderDao(): FolderDao
  abstract fun metadataDao(): MetadataDao
}
