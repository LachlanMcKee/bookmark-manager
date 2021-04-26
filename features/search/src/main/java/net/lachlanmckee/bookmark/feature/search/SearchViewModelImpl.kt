package net.lachlanmckee.bookmark.feature.search

import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import net.lachlanmckee.bookmark.feature.StandardViewModel
import net.lachlanmckee.bookmark.feature.model.Navigation
import net.lachlanmckee.bookmark.feature.search.SearchViewModel.Event
import net.lachlanmckee.bookmark.feature.search.SearchViewModel.State
import net.lachlanmckee.bookmark.feature.search.model.*
import net.lachlanmckee.bookmark.feature.search.service.repository.SearchRepository
import net.lachlanmckee.bookmark.service.repository.BookmarkRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class SearchViewModelImpl @Inject constructor(
  private val bookmarkRepository: BookmarkRepository,
  private val searchRepository: SearchRepository
) : StandardViewModel<State, Event>(), SearchViewModel {

  private val currentQueryFlowable: MutableStateFlow<QueryMetadata> =
    MutableStateFlow(
      QueryMetadata(
        query = "",
        selectedMetadata = linkedSetOf()
      )
    )

  override val initialState: State = State.emptyState

  override fun createState(): Flow<State> =
    bookmarkRepository
      .getAllMetadata()
      .flatMapLatest { allMetadataModelList ->
        val allMetadata = allMetadataModelList.map {
          SearchMetadata(it.id, createSearchText(it.name, emptyList()))
        }

        currentQueryFlowable
          .mapLatest { queryMetadata ->
            getBookmarks(queryMetadata, allMetadata)
          }
      }

  override val eventConsumer: (Event) -> Unit = { event ->
    when (event) {
      is Event.Back -> navigate(Navigation.Back)
      is Event.HomeClicked -> navigate(Navigation.Home)
      is Event.SearchClicked -> navigate(Navigation.Search)
      is Event.SettingsClicked -> navigate(Navigation.Settings)
      is Event.ContentClicked -> contentClicked(event.content)
      is Event.MetadataRowItemClicked -> metadataRowItemClicked(event.metadata)
      is Event.SearchTextChanged -> searchTextChanged(event.searchText)
    }
  }

  private fun getBookmarks(
    queryMetadata: QueryMetadata,
    allMetadata: List<SearchMetadata>
  ): State {
    val terms = queryMetadata
      .query
      .split("\\s".toRegex())
      .filter { it.isNotEmpty() }

    return State(
      query = queryMetadata.query,
      metadata = allMetadata.map { metadata ->
        SelectableMetadata(queryMetadata.selectedMetadata.contains(metadata), metadata)
      },
      contentList = searchRepository
        .getBookmarksByQuery(
          terms = terms,
          metadataIds = queryMetadata.selectedMetadata.map { it.id }
        )
        .map { bookmarksData ->
          Timber.d("Search. query: ${queryMetadata.query}, result: $bookmarksData")

          bookmarksData.map { bookmark ->
            SearchContent.BookmarkContent(
              id = bookmark.id,
              name = createSearchText(bookmark.name, terms),
              link = createSearchText(bookmark.link, terms),
              metadata = bookmark.metadata.map {
                SearchMetadata(
                  id = it.id,
                  name = createSearchText(it.name, terms)
                )
              }
            )
          }
        }
    )
  }

  private fun contentClicked(content: SearchContent) {
    when (content) {
      is SearchContent.BookmarkContent -> {
        navigate(Navigation.Bookmark(content.link.fullText))
      }
    }
  }

  private fun metadataRowItemClicked(metadata: SearchMetadata) {
    val previousQuery = currentQueryFlowable.value
    currentQueryFlowable.value = previousQuery.copy(
      selectedMetadata = previousQuery.selectedMetadata.let { selectedMetadata ->
        if (selectedMetadata.contains(metadata)) {
          selectedMetadata.minus(metadata)
        } else {
          selectedMetadata.plus(metadata)
        }
      }
    )
  }

  private fun searchTextChanged(text: String) {
    currentQueryFlowable.value = currentQueryFlowable.value.copy(
      query = text
    )
  }

  private fun createSearchText(fullText: String, terms: List<String>): SearchText {
    val segments = mutableListOf<TextSegment>()
    val boldRanges = mutableListOf<IntRange>()

    terms
      .forEach { querySegment ->
        var currentIndex = 0
        while (true) {
          val nextIndex = fullText.indexOf(querySegment, currentIndex, ignoreCase = true)

          Timber.d("nextIndex: $nextIndex")
          if (nextIndex == -1) {
            break
          }

          boldRanges.add(IntRange(nextIndex, nextIndex + querySegment.length))
          currentIndex = nextIndex + 1
        }
      }

    Timber.d("Bold ranges: $boldRanges")

    return SearchText(
      fullText = fullText,
      segments = listOf(TextSegment.Standard(fullText))
    )
  }

  private data class QueryMetadata(
    val query: String,
    val selectedMetadata: Set<SearchMetadata>
  )
}
