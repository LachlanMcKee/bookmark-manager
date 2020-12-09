package net.lachlanmckee.bookmark.service.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
  @Query("SELECT * FROM bookmark WHERE folderId is NULL")
  fun getTopLevelBookmarks(): Flow<List<BookmarkEntity>>

  @Query("SELECT * FROM bookmark WHERE folderId = :folderId")
  fun getBookmarksWithinFolder(folderId: Int): Flow<List<BookmarkEntity>>

  @Query("SELECT * FROM bookmark WHERE bookmarkId IN (:bookmarkIds)")
  fun loadAllByIds(bookmarkIds: IntArray): Flow<List<BookmarkEntity>>

  @Transaction
  @Query("SELECT * FROM bookmark WHERE name LIKE '%' || :nameOrUrl || '%' or link LIKE '%' || :nameOrUrl || '%'")
  fun findByNameOrLink(nameOrUrl: String): Flow<List<BookmarkWithMetadata>>

  @Insert
  suspend fun insertAll(vararg bookmarkEntities: BookmarkEntity)

  @Query("DELETE FROM bookmark WHERE bookmarkId IN (:bookmarkIds)")
  suspend fun deleteByIds(bookmarkIds: IntArray)

  @Query("DELETE FROM bookmark")
  suspend fun deleteAll()
}
