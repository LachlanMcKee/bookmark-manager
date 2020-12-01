package net.lachlanmckee.bookmark.service.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.lachlanmckee.bookmark.service.model.Folder
import net.lachlanmckee.bookmark.service.persistence.*
import javax.inject.Inject

interface FolderRepository {
  fun getFolders(parentId: Int?): Flow<List<Folder>>

  suspend fun addFolder()

  suspend fun removeFolders(selectedIds: Set<Int>)
}

class FolderRepositoryImpl @Inject constructor(
  private val folderDao: FolderDao
) : FolderRepository {

  override fun getFolders(parentId: Int?): Flow<List<Folder>> {
    val foldersFlow = if (parentId != null) {
      folderDao.getChildFolders(parentId)
    } else {
      folderDao.getTopLevelFolders()
    }
    return foldersFlow
      .map { folderEntities ->
        folderEntities.map { entity ->
          Folder(
            id = entity.uid,
            parentId = entity.parentId,
            name = entity.name
          )
        }
      }
  }

  override suspend fun addFolder() {
    folderDao.deleteAll()
    folderDao.insertAll(
      FolderEntity(1, null, "Root Folder 1"),
      FolderEntity(2, null, "Root Folder 2"),
      FolderEntity(3, 1, "Root Folder 1 - Child 1"),
      FolderEntity(4, 1, "Root Folder 1 - Child 2"),
      FolderEntity(5, 2, "Root Folder 2 - Child 1")
    )
  }

  override suspend fun removeFolders(selectedIds: Set<Int>) {
    folderDao.deleteByIds(selectedIds.toIntArray())
  }
}
