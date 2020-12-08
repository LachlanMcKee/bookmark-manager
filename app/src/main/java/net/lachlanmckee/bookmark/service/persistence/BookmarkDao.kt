package net.lachlanmckee.bookmark.service.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
  @Query("SELECT * FROM bookmark WHERE folderId is NULL")
  fun getTopLevelBookmarks(): Flow<List<BookmarkEntity>>

  @Query("SELECT * FROM bookmark WHERE folderId = :folderId")
  fun getBookmarksWithinFolder(folderId: Int): Flow<List<BookmarkEntity>>

  @Query("SELECT * FROM bookmark WHERE uid IN (:bookmarkIds)")
  fun loadAllByIds(bookmarkIds: IntArray): Flow<List<BookmarkEntity>>

  @Query("SELECT * FROM bookmark WHERE name LIKE '%' || :nameOrUrl || '%' or link LIKE '%' || :nameOrUrl || '%'")
  fun findByNameOrLink(nameOrUrl: String): Flow<List<BookmarkEntity>>

  @Insert
  suspend fun insertAll(vararg bookmarkEntities: BookmarkEntity)

  @Query("DELETE FROM bookmark WHERE uid IN (:bookmarkIds)")
  suspend fun deleteByIds(bookmarkIds: IntArray)

  @Query("DELETE FROM bookmark")
  suspend fun deleteAll()
}
