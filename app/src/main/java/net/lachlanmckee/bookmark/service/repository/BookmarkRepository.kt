package net.lachlanmckee.bookmark.service.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.lachlanmckee.bookmark.service.model.Bookmark
import net.lachlanmckee.bookmark.service.persistence.BookmarkDao
import net.lachlanmckee.bookmark.service.persistence.BookmarkEntity
import javax.inject.Inject

interface BookmarkRepository {
  fun getBookmarksByQuery(text: String): Flow<List<Bookmark>>

  fun getBookmarksByFolder(folderId: Int?): Flow<List<Bookmark>>

  suspend fun addBookmark()

  suspend fun removeBookmarks(selectedIds: Set<Int>)
}

class BookmarkRepositoryImpl @Inject constructor(
  private val bookmarkDao: BookmarkDao
) : BookmarkRepository {

  override fun getBookmarksByQuery(text: String): Flow<List<Bookmark>> {
    return bookmarkDao.findByNameOrLink(text)
      .map { bookmarkEntities ->
        bookmarkEntities.map(::mapToBookmark)
      }
  }

  override fun getBookmarksByFolder(folderId: Int?): Flow<List<Bookmark>> {
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

  private fun mapToBookmark(entity: BookmarkEntity): Bookmark {
    return Bookmark(id = entity.uid, name = entity.name, link = entity.link)
  }

  override suspend fun addBookmark() {
    bookmarkDao.deleteAll()
    bookmarkDao.insertAll(
      BookmarkEntity(
        name = "Google",
        link = "https://www.google.com/",
        folderId = null
      ),
      BookmarkEntity(
        name = "Amazon",
        link = "https://www.amazon.co.uk/",
        folderId = null
      ),
      BookmarkEntity(
        name = "Amazon Very Long",
        link = "https://www.amazon.co.uk/abc/123/def/456",
        folderId = null
      ),
      BookmarkEntity(
        name = "Amazon Very Long",
        link = "https://www.amazon.co.uk/abc/123/def/456",
        folderId = 1
      ),
      BookmarkEntity(
        name = "Amazon Very Long",
        link = "https://www.amazon.co.uk/abc/123/def/456",
        folderId = 2
      )
    )
  }

  override suspend fun removeBookmarks(selectedIds: Set<Int>) {
    bookmarkDao.deleteByIds(selectedIds.toIntArray())
  }
}
