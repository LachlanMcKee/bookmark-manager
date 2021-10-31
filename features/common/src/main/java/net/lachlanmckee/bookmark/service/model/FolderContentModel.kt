package net.lachlanmckee.bookmark.service.model

data class FolderContentModel(
  val folder: FolderModel?,
  val items: List<FolderItemModel>
)
