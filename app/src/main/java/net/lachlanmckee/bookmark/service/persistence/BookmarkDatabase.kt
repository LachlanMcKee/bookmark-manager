package net.lachlanmckee.bookmark.service.persistence

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [
        BookmarkEntity::class,
        FolderEntity::class
    ]
)
abstract class BookmarkDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun folderDao(): FolderDao
}