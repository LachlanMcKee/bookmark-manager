package net.lachlanmckee.bookmark.service.repository

import kotlinx.coroutines.flow.Flow
import net.lachlanmckee.bookmark.service.model.FolderContentModel
import net.lachlanmckee.bookmark.service.model.MetadataModel

interface BookmarkRepository {
  fun getFolderContent(folderId: Long?): Flow<FolderContentModel>

  fun getAllMetadata(): Flow<List<MetadataModel>>

  suspend fun saveBookmark(name: String, url: String)

  suspend fun resetData()

  suspend fun removeContent(folderIds: Set<Long>, bookmarkIds: Set<Long>)
}
