package net.lachlanmckee.bookmark.service.persistence

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmark")
    fun getAll(): Flow<List<BookmarkEntity>>

    @Query("SELECT * FROM bookmark WHERE uid IN (:bookmarkIds)")
    fun loadAllByIds(bookmarkIds: IntArray): Flow<List<BookmarkEntity>>

    @Query("SELECT * FROM bookmark WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): Flow<BookmarkEntity>

    @Insert
    suspend fun insertAll(vararg bookmarkEntities: BookmarkEntity)

    @Query("DELETE FROM bookmark WHERE uid IN (:bookmarkIds)")
    suspend fun deleteByIds(bookmarkIds: IntArray)
}