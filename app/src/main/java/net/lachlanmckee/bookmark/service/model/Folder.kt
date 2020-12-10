package net.lachlanmckee.bookmark.service.model

data class Folder(
  val id: Long,
  val parentId: Long?,
  val name: String
)
