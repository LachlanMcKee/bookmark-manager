package net.lachlanmckee.bookmark.feature.search.model

internal sealed class TextSegment {
  abstract val text: String

  data class Standard(override val text: String) : TextSegment()
  data class Highlighted(override val text: String) : TextSegment()
}
