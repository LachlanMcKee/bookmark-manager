package net.lachlanmckee.bookmark.feature.search

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import net.lachlanmckee.bookmark.feature.Navigator
import net.lachlanmckee.bookmark.feature.RootViewModel
import net.lachlanmckee.bookmark.service.repository.BookmarkRepository
import timber.log.Timber
import javax.inject.Inject

class SearchViewModel @Inject constructor(
  private val bookmarkRepository: BookmarkRepository,
  private val navigator: Navigator
) : ViewModel(), RootViewModel {

  private val currentQueryFlowable: MutableStateFlow<QueryMetadata> =
    MutableStateFlow(
      QueryMetadata(
        query = "",
        selectedMetadata = linkedSetOf()
      )
    )

  @ExperimentalStdlibApi
  @ExperimentalCoroutinesApi
  val state: LiveData<Results> by lazy {
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

  @ExperimentalStdlibApi
  private fun getBookmarks(
    queryMetadata: QueryMetadata,
    allMetadata: List<SearchMetadata>
  ): Results {
    val terms = queryMetadata
      .query
      .split("\\s".toRegex())
      .filter { it.isNotEmpty() }

    return Results(
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

  fun contentClicked(content: Content) {
    when (content) {
      is Content.BookmarkContent -> {
        navigator.openBookmark(content.link.fullText)
      }
    }
  }

  fun metadataRowItemClicked(metadata: SearchMetadata) {
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

  override fun homeClicked() {
    navigator.home()
  }

  override fun searchClicked() {
    navigator.search()
  }

  override fun settingsClicked() {
    navigator.settings()
  }

  fun backPressed() {
    navigator.back()
  }

  fun searchTextChanged(text: String) {
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

  data class Results(
    val query: String,
    val metadata: List<SelectableMetadata>,
    val contentList: Flow<PagingData<Content>>
  )

  private val emptyResults = Results(
    query = "",
    metadata = emptyList(),
    contentList = emptyFlow()
  )

  sealed class Content {
    data class BookmarkContent(
      val id: Long,
      val name: SearchText,
      val link: SearchText,
      val metadata: List<SearchMetadata>
    ) : Content()
  }

  data class SearchMetadata(
    val id: Long,
    val name: SearchText
  )

  data class SelectableMetadata(
    val isSelected: Boolean,
    val metadata: SearchMetadata
  )

  data class QueryMetadata(
    val query: String,
    val selectedMetadata: Set<SearchMetadata>
  )

  data class SearchText(
    val fullText: String,
    val segments: List<TextSegment>
  )

  sealed class TextSegment {
    abstract val text: String

    data class Standard(override val text: String) : TextSegment()
    data class Highlighted(override val text: String) : TextSegment()
  }
}
