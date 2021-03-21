package net.lachlanmckee.bookmark.feature.search.model

internal data class SelectableMetadata(
  val isSelected: Boolean,
  val metadata: SearchMetadata
)
