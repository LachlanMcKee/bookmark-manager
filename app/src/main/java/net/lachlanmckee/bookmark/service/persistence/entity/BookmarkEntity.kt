package net.lachlanmckee.bookmark.service.persistence.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
  tableName = "bookmark",
  foreignKeys = [
    ForeignKey(
      entity = FolderEntity::class,
      parentColumns = ["folderId"],
      childColumns = ["folderId"],
      onDelete = ForeignKey.CASCADE
    ),
  ]
)
data class BookmarkEntity(
  @PrimaryKey(autoGenerate = true) val bookmarkId: Long,
  val name: String,
  val link: String,
  val folderId: Long?
) {
  constructor(name: String, link: String, folderId: Long?) : this(0, name, link, folderId)
}
