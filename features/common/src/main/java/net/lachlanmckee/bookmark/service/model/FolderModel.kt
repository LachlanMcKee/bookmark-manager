package net.lachlanmckee.bookmark.service.model

data class FolderModel(
  val id: Long,
  val parentId: Long?,
  val name: String
)
