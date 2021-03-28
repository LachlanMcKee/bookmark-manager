package net.lachlanmckee.bookmark.feature.search.service.persistence.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import net.lachlanmckee.bookmark.service.persistence.entity.BookmarkEntity
import net.lachlanmckee.bookmark.service.persistence.entity.BookmarkMetadataCrossRef
import net.lachlanmckee.bookmark.service.persistence.entity.BookmarkWithMetadata
import net.lachlanmckee.bookmark.service.persistence.entity.MetadataEntity

@Dao
abstract class SearchDao {
  @Transaction
  @RawQuery(
    observedEntities = [
      BookmarkEntity::class,
      MetadataEntity::class,
      BookmarkMetadataCrossRef::class
    ]
  )
  abstract fun findByTextRawQuery(
    query: SupportSQLiteQuery
  ): PagingSource<Int, BookmarkWithMetadata>

  open fun findByTermsAndMetadataIds(
    terms: List<String>,
    metadataIds: List<Long>
  ): PagingSource<Int, BookmarkWithMetadata> {
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
  }
}
