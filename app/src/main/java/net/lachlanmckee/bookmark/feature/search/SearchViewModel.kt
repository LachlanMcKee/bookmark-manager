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
    MutableStateFlow(QueryMetadata(""))

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
    return bookmarkRepository.getBookmarksByQuery(queryMetadata.query)
      .map { bookmarks ->
        Timber.d("Search. query: ${queryMetadata.query}, result: $bookmarks")
        if (bookmarks.isNotEmpty()) {
          val contentList = buildList {
            bookmarks.forEach { bookmark ->
              add(
                Content.BookmarkContent(
                  id = bookmark.id,
                  name = bookmark.name,
                  link = bookmark.link,
                  metadataNames = bookmark.metadata.map { it.name }
                )
              )
            }
          }
          State.Results(
            query = queryMetadata.query,
            contentList = contentList
          )
        } else {
          State.Results(
            query = queryMetadata.query,
            contentList = emptyList()
          )
        }
      }
  }

  fun contentClicked(content: Content) {
    when (content) {
      is Content.BookmarkContent -> {
        navigator.openBookmark(content.link)
      }
    }
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
    currentQueryFlowable.value = QueryMetadata(text)
  }

  sealed class State {
    abstract val query: String

    object Empty : State() {
      override val query: String = ""
    }

    data class Results(
      override val query: String,
      val contentList: List<Content>
    ) : State()
  }

  sealed class Content {
    data class BookmarkContent(
      val id: Long,
      val name: String,
      val link: String,
      val metadataNames: List<String>
    ) : Content()
  }

  data class QueryMetadata(
    val query: String
  )
}
