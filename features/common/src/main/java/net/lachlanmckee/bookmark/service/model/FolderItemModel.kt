package net.lachlanmckee.bookmark.service.model

sealed class FolderItemModel {
  data class Folder(val folder: FolderModel) : FolderItemModel()
  data class Bookmark(val bookmark: BookmarkModel) : FolderItemModel()
}
