package net.lachlanmckee.bookmark.service.persistence.dao

import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow
import net.lachlanmckee.bookmark.service.persistence.entity.BookmarkEntity
import net.lachlanmckee.bookmark.service.persistence.entity.BookmarkMetadataCrossRef
import net.lachlanmckee.bookmark.service.persistence.entity.BookmarkWithMetadata
import net.lachlanmckee.bookmark.service.persistence.entity.MetadataEntity

@Dao
abstract class BookmarkDao {
  @Query("SELECT * FROM bookmark WHERE folderId is NULL")
  abstract fun getTopLevelBookmarks(): Flow<List<BookmarkEntity>>

  @Query("SELECT * FROM bookmark WHERE folderId = :folderId")
  abstract fun getBookmarksWithinFolder(folderId: Long): Flow<List<BookmarkEntity>>

  @Query("SELECT * FROM bookmark WHERE bookmarkId IN (:bookmarkIds)")
  abstract fun loadAllByIds(bookmarkIds: LongArray): Flow<List<BookmarkEntity>>

  @Transaction
  @RawQuery(
    observedEntities = [
      BookmarkEntity::class,
      MetadataEntity::class,
      BookmarkMetadataCrossRef::class
    ]
  )
  abstract fun findByTextRawQuery(query: SupportSQLiteQuery): Flow<List<BookmarkWithMetadata>>

  @OptIn(ExperimentalStdlibApi::class)
  open fun findByTermsAndMetadataIds(
    terms: List<String>,
    metadataIds: List<Long>
  ): Flow<List<BookmarkWithMetadata>> {
    val query = buildString {
      appendLine("SELECT * FROM bookmark")
      appendLine("WHERE")

      metadataIds.forEachIndexed { index, _ ->
        if (index != 0) {
          appendLine("AND ")
        }
        appendLine("bookmarkId in (")
        appendLine("SELECT BookmarkMetadataCrossRef.bookmarkId")
        appendLine("FROM BookmarkMetadataCrossRef")
        append("WHERE BookmarkMetadataCrossRef.metadataId = ?")
        appendLine(")")
      }

      if (metadataIds.isNotEmpty() && terms.isNotEmpty()) {
        appendLine("AND ")
      }

      terms.forEachIndexed { index, _ ->
        if (index != 0) {
          appendLine("AND ")
        }
        appendLine("(")
        appendLine("name LIKE '%' || ? || '%'")
        appendLine("OR link LIKE '%' || ? || '%'")

        appendLine("OR bookmarkId in (")
        appendLine("SELECT BookmarkMetadataCrossRef.bookmarkId")
        appendLine("FROM BookmarkMetadataCrossRef")
        appendLine("INNER JOIN Metadata ON BookmarkMetadataCrossRef.metadataId = Metadata.metadataId")
        appendLine("WHERE Metadata.name LIKE '%' || ? || '%'")
        appendLine(")")
        appendLine(")")
      }
    }

    val bindArgs: List<Any> = buildList {
      if (metadataIds.isNotEmpty()) {
        addAll(metadataIds)
      }
      terms.forEach {
        add(it)
        add(it)
        add(it)
      }
    }

    return findByTextRawQuery(SimpleSQLiteQuery(query, bindArgs.toTypedArray()))

//    return findByTextRawQuery(
//      SimpleSQLiteQuery(
//        """
//        SELECT *
//        FROM bookmark
//        WHERE name LIKE '%' || :nameOrUrl || '%'
//        or link LIKE '%' || :nameOrUrl || '%'
//        or bookmarkId in (
//            SELECT BookmarkMetadataCrossRef.bookmarkId
//            FROM BookmarkMetadataCrossRef
//            INNER JOIN Metadata ON BookmarkMetadataCrossRef.metadataId = Metadata.metadataId
//            WHERE Metadata.name LIKE '%' || :nameOrUrl || '%'
//        )
//      """.trimIndent()
//      )
  }

  @Transaction
  open suspend fun insert(bookmarkEntity: BookmarkEntity, vararg metadataIds: Long) {
    val bookmarkId = insert(bookmarkEntity)
    insertAll(
      *metadataIds
        .map { metadataId ->
          BookmarkMetadataCrossRef(
            bookmarkId = bookmarkId,
            metadataId = metadataId
          )
        }
        .toTypedArray()
    )
  }

  @Insert
  abstract suspend fun insert(bookmarkEntity: BookmarkEntity): Long

  @Insert
  abstract suspend fun insertAll(vararg crossRef: BookmarkMetadataCrossRef)

  @Query("DELETE FROM bookmark WHERE bookmarkId IN (:bookmarkIds)")
  abstract suspend fun deleteByIds(bookmarkIds: LongArray)

  @Query("DELETE FROM bookmark")
  abstract suspend fun deleteAll()
}
