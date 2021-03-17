package net.lachlanmckee.bookmark.feature.search

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import net.lachlanmckee.bookmark.feature.BookmarkViewModel

interface SearchViewModel : BookmarkViewModel<SearchViewModel.State, SearchViewModel.Event> {

  data class State(
    val query: String,
    val metadata: List<SelectableMetadata>,
    val contentList: Flow<PagingData<Content>>
  )

  sealed class Event {
    object Back : Event()
    object HomeClicked : Event()
    object SearchClicked : Event()
    object SettingsClicked : Event()

    data class MetadataRowItemClicked(val metadata: SearchMetadata) : Event()
    data class SearchTextChanged(val searchText: String) : Event()
    data class ContentClicked(val content: Content.BookmarkContent) : Event()
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

  data class SelectableMetadata(
    val isSelected: Boolean,
    val metadata: SearchMetadata
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
