package net.lachlanmckee.bookmark.service.model

data class BookmarkModel(
  val id: Long,
  val name: String,
  val link: String,
  val metadata: List<MetadataModel>
)
