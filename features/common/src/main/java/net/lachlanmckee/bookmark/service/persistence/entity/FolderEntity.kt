package net.lachlanmckee.bookmark.service.persistence.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "folder")
data class FolderEntity(
  @PrimaryKey(autoGenerate = true) val folderId: Long,
  var parentId: Long?,
  var name: String
) {
  constructor(parentId: Long?, name: String) : this(0, parentId, name)
}
