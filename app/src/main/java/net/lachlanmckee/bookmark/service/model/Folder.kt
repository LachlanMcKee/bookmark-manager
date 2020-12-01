package net.lachlanmckee.bookmark.service.model

data class Folder(
  val id: Int,
  val parentId: Int?,
  val name: String
)
