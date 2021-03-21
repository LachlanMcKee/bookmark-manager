package net.lachlanmckee.bookmark.feature.home.model

internal data class FolderMetadata(
  val folderId: Long,
  val parent: FolderMetadata?
)
