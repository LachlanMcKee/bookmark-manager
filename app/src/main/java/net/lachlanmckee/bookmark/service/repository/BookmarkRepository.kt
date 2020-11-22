package net.lachlanmckee.bookmark.service.repository

import kotlinx.coroutines.flow.*
import net.lachlanmckee.bookmark.service.model.Bookmark
import javax.inject.Inject

interface BookmarkRepository {
    fun getBookmarks(): Flow<List<Bookmark>>

    suspend fun addBookmark()
}

class BookmarkRepositoryImpl @Inject constructor() : BookmarkRepository {
    private val bookmarkFlow = MutableStateFlow<List<Bookmark>>(emptyList())

    override fun getBookmarks(): Flow<List<Bookmark>> {
        return bookmarkFlow
    }

    override suspend fun addBookmark() {
        bookmarkFlow.value = bookmarkFlow.value.plus(
            listOf(
                Bookmark("Google", "https://www.google.com/"),
                Bookmark("Amazon", "https://www.amazon.co.uk/"),
                Bookmark("Amazon Very Long", "https://www.amazon.co.uk/abc/123/def/456")
            )
        )
    }
}
