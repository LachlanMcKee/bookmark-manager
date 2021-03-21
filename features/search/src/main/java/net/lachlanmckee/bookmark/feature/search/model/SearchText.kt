package net.lachlanmckee.bookmark.feature.search.model

internal data class SearchText(
  val fullText: String,
  val segments: List<TextSegment>
)
