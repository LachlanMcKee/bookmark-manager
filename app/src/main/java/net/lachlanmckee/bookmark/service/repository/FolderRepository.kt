package net.lachlanmckee.bookmark.service.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.lachlanmckee.bookmark.service.model.Folder
import net.lachlanmckee.bookmark.service.persistence.dao.FolderDao
import javax.inject.Inject

interface FolderRepository {
  fun getFolders(parentId: Long?): Flow<List<Folder>>

  suspend fun addFolder()

  suspend fun removeFolders(selectedIds: Set<Long>)
}

class FolderRepositoryImpl @Inject constructor(
  private val folderDao: FolderDao
) : FolderRepository {

  override fun getFolders(parentId: Long?): Flow<List<Folder>> {
    val foldersFlow = if (parentId != null) {
      folderDao.getChildFolders(parentId)
    } else {
      folderDao.getTopLevelFolders()
    }
    return foldersFlow
      .map { folderEntities ->
        folderEntities.map { entity ->
          Folder(
            id = entity.folderId,
            parentId = entity.parentId,
            name = entity.name
          )
        }
      }
  }

  override suspend fun addFolder() {
  }

  override suspend fun removeFolders(selectedIds: Set<Long>) {
    folderDao.deleteByIds(selectedIds.toLongArray())
  }
}
