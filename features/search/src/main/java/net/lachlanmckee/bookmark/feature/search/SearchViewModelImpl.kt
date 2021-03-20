package net.lachlanmckee.bookmark.feature.search

import androidx.lifecycle.*
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import net.lachlanmckee.bookmark.feature.Navigation
import net.lachlanmckee.bookmark.feature.search.SearchViewModel.*
import net.lachlanmckee.bookmark.service.repository.BookmarkRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class SearchViewModelImpl @Inject constructor(
  private val bookmarkRepository: BookmarkRepository
) : ViewModel(), SearchViewModel {

  private val navigationFlow = MutableLiveData<Navigation>()
  private val currentQueryFlowable: MutableStateFlow<QueryMetadata> =
    MutableStateFlow(
      QueryMetadata(
        query = "",
        selectedMetadata = linkedSetOf()
      )
    )

  override val state: LiveData<State> by lazy {
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
      .onStart { emit(emptyResults) }
      .asLiveData(viewModelScope.coroutineContext)
  }

  override val eventConsumer: (Event) -> Unit = { event ->
    when (event) {
      is Event.Back -> navigationFlow.value = Navigation.Back
      is Event.HomeClicked -> navigationFlow.value = Navigation.Home
      is Event.SearchClicked -> navigationFlow.value = Navigation.Search
      is Event.SettingsClicked -> navigationFlow.value = Navigation.Settings
      is Event.ContentClicked -> contentClicked(event.content)
      is Event.MetadataRowItemClicked -> metadataRowItemClicked(event.metadata)
      is Event.SearchTextChanged -> searchTextChanged(event.searchText)
    }
  }

  override val navigation: LiveData<Navigation>
    get() = navigationFlow

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
      contentList = bookmarkRepository
        .getBookmarksByQuery(
          terms = terms,
          metadataIds = queryMetadata.selectedMetadata.map { it.id }
        )
        .map { bookmarksData ->
          Timber.d("Search. query: ${queryMetadata.query}, result: $bookmarksData")

          bookmarksData.map { bookmark ->
            Content.BookmarkContent(
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

  private fun contentClicked(content: Content) {
    when (content) {
      is Content.BookmarkContent -> {
        navigationFlow.value = Navigation.Bookmark(content.link.fullText)
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

  private val emptyResults = State(
    query = "",
    metadata = emptyList(),
    contentList = emptyFlow()
  )

  private data class QueryMetadata(
    val query: String,
    val selectedMetadata: Set<SearchMetadata>
  )
}
