package net.lachlanmckee.bookmark.service.model

sealed class FolderContentModel {
  data class Folder(val folder: FolderModel) : FolderContentModel()
  data class Bookmark(val bookmark: BookmarkModel) : FolderContentModel()
}
