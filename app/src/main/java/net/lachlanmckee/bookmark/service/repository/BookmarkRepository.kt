package net.lachlanmckee.bookmark.service.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.lachlanmckee.bookmark.service.model.Bookmark
import net.lachlanmckee.bookmark.service.persistence.BookmarkDao
import net.lachlanmckee.bookmark.service.persistence.BookmarkEntity
import javax.inject.Inject

interface BookmarkRepository {
    fun getBookmarks(): Flow<List<Bookmark>>

    suspend fun addBookmark()

    suspend fun removeBookmarks(selectedIds: Set<Int>)
}

class BookmarkRepositoryImpl @Inject constructor(
    private val bookmarkDao: BookmarkDao
) : BookmarkRepository {

    override fun getBookmarks(): Flow<List<Bookmark>> {
        return bookmarkDao
            .getAll()
            .map { bookmarkEntities ->
                bookmarkEntities.map { entity ->
                    Bookmark(id = entity.uid, name = entity.name, link = entity.link)
                }
            }
    }

    override suspend fun addBookmark() {
        bookmarkDao.insertAll(
            BookmarkEntity("Google", "https://www.google.com/"),
            BookmarkEntity("Amazon", "https://www.amazon.co.uk/"),
            BookmarkEntity("Amazon Very Long", "https://www.amazon.co.uk/abc/123/def/456")
        )
    }

    override suspend fun removeBookmarks(selectedIds: Set<Int>) {
        bookmarkDao.deleteByIds(selectedIds.toIntArray())
    }
}
