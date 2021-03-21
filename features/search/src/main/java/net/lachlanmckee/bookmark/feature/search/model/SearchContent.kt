package net.lachlanmckee.bookmark.feature.search.model

internal sealed class SearchContent {
  data class BookmarkContent(
    val id: Long,
    val name: SearchText,
    val link: SearchText,
    val metadata: List<SearchMetadata>
  ) : SearchContent()
}
