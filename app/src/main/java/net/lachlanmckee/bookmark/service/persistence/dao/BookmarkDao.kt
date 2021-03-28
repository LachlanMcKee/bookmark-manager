package net.lachlanmckee.bookmark.service.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import net.lachlanmckee.bookmark.service.persistence.entity.BookmarkEntity
import net.lachlanmckee.bookmark.service.persistence.entity.BookmarkMetadataCrossRef
import net.lachlanmckee.bookmark.service.persistence.entity.BookmarkWithMetadata

@Dao
abstract class BookmarkDao {
  @Query("SELECT * FROM bookmark WHERE folderId is NULL")
  abstract fun getTopLevelBookmarks(): Flow<List<BookmarkWithMetadata>>

  @Query("SELECT * FROM bookmark WHERE folderId = :folderId")
  abstract fun getBookmarksWithinFolder(folderId: Long): Flow<List<BookmarkWithMetadata>>

  @Transaction
  open suspend fun insert(bookmarkEntity: BookmarkEntity, vararg metadataIds: Long) {
    val bookmarkId = insert(bookmarkEntity)
    insertAll(
      *metadataIds
        .map { metadataId ->
          BookmarkMetadataCrossRef(
            bookmarkId = bookmarkId,
            metadataId = metadataId
          )
        }
        .toTypedArray()
    )
  }

  @Insert
  abstract suspend fun insert(bookmarkEntity: BookmarkEntity): Long

  @Insert
  abstract suspend fun insertAll(vararg crossRef: BookmarkMetadataCrossRef)

  @Query("DELETE FROM bookmark WHERE bookmarkId IN (:bookmarkIds)")
  abstract suspend fun deleteByIds(bookmarkIds: LongArray)

  @Query("DELETE FROM bookmark")
  abstract suspend fun deleteAll()
}
