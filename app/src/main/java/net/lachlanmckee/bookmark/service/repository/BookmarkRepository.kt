package net.lachlanmckee.bookmark.service.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import net.lachlanmckee.bookmark.service.model.BookmarkModel
import net.lachlanmckee.bookmark.service.model.MetadataModel
import net.lachlanmckee.bookmark.service.persistence.dao.BookmarkDao
import net.lachlanmckee.bookmark.service.persistence.dao.FolderDao
import net.lachlanmckee.bookmark.service.persistence.dao.MetadataDao
import net.lachlanmckee.bookmark.service.persistence.entity.BookmarkEntity
import net.lachlanmckee.bookmark.service.persistence.entity.BookmarkWithMetadata
import net.lachlanmckee.bookmark.service.persistence.entity.FolderEntity
import net.lachlanmckee.bookmark.service.persistence.entity.MetadataEntity
import javax.inject.Inject

interface BookmarkRepository {
  fun getBookmarksByQuery(
    terms: List<String>,
    metadataIds: List<Long>
  ): Flow<List<BookmarkModel>>

  fun getBookmarksByFolder(folderId: Long?): Flow<List<BookmarkModel>>

  fun getAllMetadata(): Flow<List<MetadataModel>>

  suspend fun addBookmark()

  suspend fun removeBookmarks(selectedIds: Set<Long>)
}

class BookmarkRepositoryImpl @Inject constructor(
  private val bookmarkDao: BookmarkDao,
  private val metadataDao: MetadataDao,
  private val folderDao: FolderDao
) : BookmarkRepository {

  override fun getBookmarksByQuery(
    terms: List<String>,
    metadataIds: List<Long>
  ): Flow<List<BookmarkModel>> {
    if (terms.isEmpty() && metadataIds.isEmpty()) {
      return flowOf(emptyList())
    }
    return bookmarkDao.findByTermsAndMetadataIds(terms, metadataIds)
      .map { bookmarkEntities ->
        bookmarkEntities.map(::mapToBookmark)
      }
  }

  override fun getBookmarksByFolder(folderId: Long?): Flow<List<BookmarkModel>> {
    val bookmarksFlow = if (folderId != null) {
      bookmarkDao.getBookmarksWithinFolder(folderId)
    } else {
      bookmarkDao.getTopLevelBookmarks()
    }
    return bookmarksFlow
      .map { bookmarkEntities ->
        bookmarkEntities.map(::mapToBookmark)
      }
  }

  override fun getAllMetadata(): Flow<List<MetadataModel>> {
    return metadataDao.getAllMetadata().map { metadataList ->
      metadataList.map { MetadataModel(it.metadataId, it.name) }
    }
  }

  private fun mapToBookmark(entity: BookmarkEntity): BookmarkModel {
    return BookmarkModel(
      id = entity.bookmarkId,
      name = entity.name,
      link = entity.link,
      metadata = emptyList()
    )
  }

  private fun mapToBookmark(entity: BookmarkWithMetadata): BookmarkModel {
    return BookmarkModel(
      id = entity.bookmark.bookmarkId,
      name = entity.bookmark.name,
      link = entity.bookmark.link,
      metadata = entity.metadata.map {
        MetadataModel(it.metadataId, it.name)
      }
    )
  }

  override suspend fun addBookmark() {
    bookmarkDao.deleteAll()
    metadataDao.deleteAll()
    folderDao.deleteAll()

    val folderIds = folderDao.insertAll(
      FolderEntity(null, "Root Folder 1"),
      FolderEntity(null, "Root Folder 2")
    )

    folderDao.insertAll(
      FolderEntity(folderIds[0], "Root Folder 1 - Child 1"),
      FolderEntity(folderIds[0], "Root Folder 1 - Child 2"),
      FolderEntity(folderIds[1], "Root Folder 2 - Child 1")
    )

    val metadataIds = metadataDao.insertAll(
      MetadataEntity("Food"),
      MetadataEntity("Travel"),
      MetadataEntity("Social"),
      MetadataEntity("Work"),
      MetadataEntity("News"),
      MetadataEntity("Finance"),
    )

    bookmarkDao.insert(
      BookmarkEntity(
        name = "Google",
        link = "https://www.google.com/",
        folderId = null
      ),
      metadataIds[0]
    )

    bookmarkDao.insert(
      BookmarkEntity(
        name = "Amazon",
        link = "https://www.amazon.co.uk/",
        folderId = null
      ),
      metadataIds[0],
      metadataIds[1]
    )

    bookmarkDao.insert(
      BookmarkEntity(
        name = "Amazon Very Long",
        link = "https://www.amazon.co.uk/abc/123/def/456",
        folderId = null
      ),
      metadataIds[0],
      metadataIds[2]
    )

    bookmarkDao.insert(
      BookmarkEntity(
        name = "Amazon Very Long",
        link = "https://www.amazon.co.uk/abc/123/def/456",
        folderId = folderIds[0]
      ),
      metadataIds[2]
    )

    bookmarkDao.insert(
      BookmarkEntity(
        name = "Amazon Very Long",
        link = "https://www.amazon.co.uk/abc/123/def/456",
        folderId = folderIds[1]
      ),
      metadataIds[0],
      metadataIds[1],
      metadataIds[2]
    )
  }

  override suspend fun removeBookmarks(selectedIds: Set<Long>) {
    bookmarkDao.deleteByIds(selectedIds.toLongArray())
  }
}
