package net.lachlanmckee.bookmark.feature.home.model

internal data class FolderMetadata(
  val folderId: Long,
  val folderName: String,
  val parent: FolderMetadata?
)
