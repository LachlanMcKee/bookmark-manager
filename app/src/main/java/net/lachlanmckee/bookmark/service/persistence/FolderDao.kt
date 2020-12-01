package net.lachlanmckee.bookmark.service.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {
  @Query("SELECT * FROM folder WHERE parentId is NULL")
  fun getTopLevelFolders(): Flow<List<FolderEntity>>

  @Query("SELECT * FROM folder WHERE parentId = :parentId")
  fun getChildFolders(parentId: Int): Flow<List<FolderEntity>>

  @Insert
  suspend fun insertAll(vararg folderEntities: FolderEntity)

  @Query("DELETE FROM folder WHERE uid IN (:folderIds)")
  suspend fun deleteByIds(folderIds: IntArray)

  @Query("DELETE FROM folder")
  suspend fun deleteAll()
}
