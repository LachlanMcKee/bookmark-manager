package net.lachlanmckee.bookmark.feature.search

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import net.lachlanmckee.bookmark.feature.BookmarkViewModel
import net.lachlanmckee.bookmark.feature.search.model.SearchContent
import net.lachlanmckee.bookmark.feature.search.model.SearchMetadata
import net.lachlanmckee.bookmark.feature.search.model.SelectableMetadata

internal interface SearchViewModel :
  BookmarkViewModel<SearchViewModel.State, SearchViewModel.Event> {

  data class State(
    val query: String,
    val metadata: List<SelectableMetadata>,
    val contentList: Flow<PagingData<SearchContent>>
  ) {
    companion object {
      val emptyState = State(
        query = "",
        metadata = emptyList(),
        contentList = emptyFlow()
      )
    }
  }

  sealed class Event {
    object Back : Event()
    object HomeClicked : Event()
    object SearchClicked : Event()
    object SettingsClicked : Event()

    data class MetadataRowItemClicked(val metadata: SearchMetadata) : Event()
    data class SearchTextChanged(val searchText: String) : Event()
    data class ContentClicked(val content: SearchContent.BookmarkContent) : Event()
  }
}
