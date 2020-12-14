package net.lachlanmckee.bookmark.feature.search

import androidx.lifecycle.*
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
  val state: LiveData<State> by lazy {
    currentQueryFlowable
      .flatMapLatest { queryMetadata ->
        if (queryMetadata.query.isNotEmpty()) {
          getBookmarks(queryMetadata)
        } else {
          flowOf(State.Empty)
        }
      }
      .asLiveData(viewModelScope.coroutineContext)
  }

  @ExperimentalStdlibApi
  private fun getBookmarks(queryMetadata: QueryMetadata): Flow<State> {
    val terms = queryMetadata
      .query
      .split("\\s".toRegex())
      .filter { it.isNotEmpty() }

    return bookmarkRepository
      .getBookmarksByQuery(
        terms = terms,
        metadataIds = queryMetadata.selectedMetadata.map { it.id }
      )
      .map { bookmarks ->
        Timber.d("Search. query: ${queryMetadata.query}, result: $bookmarks")
        if (bookmarks.isNotEmpty()) {
          val contentList = buildList {
            bookmarks.forEach { bookmark ->
              add(
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
              )
            }
          }
          State.Results(
            query = queryMetadata.query,
            selectedMetadata = queryMetadata.selectedMetadata.toList(),
            contentList = contentList
          )
        } else {
          State.Results(
            query = queryMetadata.query,
            selectedMetadata = queryMetadata.selectedMetadata.toList(),
            contentList = emptyList()
          )
        }
      }
  }

  fun contentClicked(content: Content) {
    when (content) {
      is Content.BookmarkContent -> {
        navigator.openBookmark(content.link.fullText)
      }
    }
  }

  fun metadataFilterClicked(metadata: SearchMetadata) {
    val previousQuery = currentQueryFlowable.value
    currentQueryFlowable.value = previousQuery.copy(
      selectedMetadata = previousQuery.selectedMetadata.minus(metadata)
    )
  }

  fun metadataRowItemClicked(metadata: SearchMetadata) {
    val previousQuery = currentQueryFlowable.value
    currentQueryFlowable.value = previousQuery.copy(
      selectedMetadata = previousQuery.selectedMetadata.plus(metadata)
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

  sealed class State {
    abstract val query: String
    abstract val selectedMetadata: List<SearchMetadata>

    object Empty : State() {
      override val query: String = ""
      override val selectedMetadata: List<SearchMetadata> = emptyList()
    }

    data class Results(
      override val query: String,
      override val selectedMetadata: List<SearchMetadata>,
      val contentList: List<Content>
    ) : State()
  }

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
