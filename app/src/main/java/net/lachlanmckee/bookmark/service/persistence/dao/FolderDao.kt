package net.lachlanmckee.bookmark.service.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import net.lachlanmckee.bookmark.service.persistence.entity.FolderEntity

@Dao
interface FolderDao {
  @Query("SELECT * FROM folder WHERE parentId is NULL")
  fun getTopLevelFolders(): Flow<List<FolderEntity>>

  @Query("SELECT * FROM folder WHERE parentId = :parentId")
  fun getChildFolders(parentId: Long): Flow<List<FolderEntity>>

  @Insert
  suspend fun insertAll(vararg folderEntities: FolderEntity): List<Long>

  @Query("DELETE FROM folder WHERE folderId IN (:folderIds)")
  suspend fun deleteByIds(folderIds: LongArray)

  @Query("DELETE FROM folder")
  suspend fun deleteAll()
}
