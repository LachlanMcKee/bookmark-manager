package net.lachlanmckee.bookmark.service.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import net.lachlanmckee.bookmark.service.model.BookmarkModel
import net.lachlanmckee.bookmark.service.model.FolderContentModel
import net.lachlanmckee.bookmark.service.model.MetadataModel

interface BookmarkRepository {
  fun getBookmarksByQuery(
    terms: List<String>,
    metadataIds: List<Long>
  ): Flow<PagingData<BookmarkModel>>

  fun getFolderContent(folderId: Long?): Flow<List<FolderContentModel>>

  fun getAllMetadata(): Flow<List<MetadataModel>>

  suspend fun resetData()

  suspend fun removeContent(folderIds: Set<Long>, bookmarkIds: Set<Long>)
}
