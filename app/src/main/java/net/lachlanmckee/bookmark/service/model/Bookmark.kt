package net.lachlanmckee.bookmark.service.model

data class Bookmark(
  val id: Long,
  val name: String,
  val link: String,
  val metadata: List<Metadata>
)
