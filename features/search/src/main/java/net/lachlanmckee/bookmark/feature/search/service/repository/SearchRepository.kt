package net.lachlanmckee.bookmark.feature.search.service.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import net.lachlanmckee.bookmark.feature.search.service.persistence.dao.SearchDao
import net.lachlanmckee.bookmark.service.model.BookmarkModel
import net.lachlanmckee.bookmark.service.model.MetadataModel
import net.lachlanmckee.bookmark.service.persistence.entity.BookmarkWithMetadata
import javax.inject.Inject

internal interface SearchRepository {
  fun getBookmarksByQuery(
    terms: List<String>,
    metadataIds: List<Long>
  ): Flow<PagingData<BookmarkModel>>
}

internal class SearchRepositoryImpl @Inject constructor(
  private val searchDao: SearchDao
) : SearchRepository {

  override fun getBookmarksByQuery(
    terms: List<String>,
    metadataIds: List<Long>
  ): Flow<PagingData<BookmarkModel>> {
    if (terms.isEmpty() && metadataIds.isEmpty()) {
      return flowOf(PagingData.empty())
    }

    return Pager(PagingConfig(pageSize = 20)) {
      searchDao
        .findByTermsAndMetadataIds(terms, metadataIds)
    }
      .flow
      .map { pagingData ->
        pagingData.map {
          mapToBookmark(it)
        }
      }
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
}
