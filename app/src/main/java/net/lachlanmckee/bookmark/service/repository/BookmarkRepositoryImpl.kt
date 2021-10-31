package net.lachlanmckee.bookmark.service.repository

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import net.lachlanmckee.bookmark.service.model.BookmarkModel
import net.lachlanmckee.bookmark.service.model.FolderContentModel
import net.lachlanmckee.bookmark.service.model.FolderItemModel
import net.lachlanmckee.bookmark.service.model.FolderModel
import net.lachlanmckee.bookmark.service.model.MetadataModel
import net.lachlanmckee.bookmark.service.persistence.BookmarkDatabase
import net.lachlanmckee.bookmark.service.persistence.dao.BookmarkDao
import net.lachlanmckee.bookmark.service.persistence.dao.FolderDao
import net.lachlanmckee.bookmark.service.persistence.dao.MetadataDao
import net.lachlanmckee.bookmark.service.persistence.entity.BookmarkEntity
import net.lachlanmckee.bookmark.service.persistence.entity.BookmarkWithMetadata
import net.lachlanmckee.bookmark.service.persistence.entity.FolderEntity
import net.lachlanmckee.bookmark.service.persistence.entity.MetadataEntity
import javax.inject.Inject

class BookmarkRepositoryImpl @Inject constructor(
  private val database: BookmarkDatabase,
  private val bookmarkDao: BookmarkDao,
  private val metadataDao: MetadataDao,
  private val folderDao: FolderDao
) : BookmarkRepository {

  override fun getFolderContent(folderId: Long?): Flow<FolderContentModel> {
    val bookmarksFlow = if (folderId != null) {
      bookmarkDao.getBookmarksWithinFolder(folderId)
    } else {
      bookmarkDao.getTopLevelBookmarks()
    }

    val foldersFlow = if (folderId != null) {
      folderDao.getChildFolders(folderId)
    } else {
      folderDao.getTopLevelFolders()
    }

    val folder = if (folderId != null) {
      folderDao
        .getFolder(folderId)
        .map { entity -> mapToFolder(entity) }
    } else {
      null
    }

    val folders = foldersFlow
      .map { folderEntities ->
        folderEntities.map { entity -> FolderItemModel.Folder(mapToFolder(entity)) }
      }

    val bookmarks = bookmarksFlow
      .map { bookmarkEntities ->
        bookmarkEntities.map { FolderItemModel.Bookmark(mapToBookmark(it)) }
      }

    return if (folder != null) {
      folder.flatMapLatest { folder ->
        folders
          .flatMapLatest { folderList ->
            bookmarks.map { bookmarkList -> folderList.plus(bookmarkList) }
          }
          .map { items ->
            FolderContentModel(folder, items)
          }
      }
    } else {
      folders
        .flatMapLatest { folderList ->
          bookmarks.map { bookmarkList -> folderList.plus(bookmarkList) }
        }
        .map { items ->
          FolderContentModel(null, items)
        }
    }
  }

  override fun getAllMetadata(): Flow<List<MetadataModel>> {
    return metadataDao.getAllMetadata().map { metadataList ->
      metadataList.map { MetadataModel(it.metadataId, it.name) }
    }
  }

  override suspend fun saveBookmark(name: String, url: String) {
    database.withTransaction {
      bookmarkDao.insert(
        BookmarkEntity(
          name = name,
          link = url,
          folderId = null
        )
      )
    }
  }

  private fun mapToFolder(entity: FolderEntity): FolderModel {
    return FolderModel(
      id = entity.folderId,
      parentId = entity.parentId,
      name = entity.name
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

  override suspend fun resetData() {
    database.withTransaction {
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

      repeat(50) {
        bookmarkDao.insert(
          BookmarkEntity(
            name = "Amazon Very Long $it",
            link = "https://www.amazon.co.uk/abc/123/def/$it",
            folderId = folderIds[1]
          ),
          metadataIds[0],
          metadataIds[1],
          metadataIds[2]
        )
      }
    }
  }

  override suspend fun removeContent(folderIds: Set<Long>, bookmarkIds: Set<Long>) {
    folderDao.deleteByIds(folderIds.toLongArray())
    bookmarkDao.deleteByIds(bookmarkIds.toLongArray())
  }
}
