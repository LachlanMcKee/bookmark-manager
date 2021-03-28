package net.lachlanmckee.bookmark.service.persistence.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE

@Entity(
  primaryKeys = ["bookmarkId", "metadataId"],
  foreignKeys = [
    ForeignKey(
      entity = BookmarkEntity::class,
      parentColumns = ["bookmarkId"],
      childColumns = ["bookmarkId"],
      onDelete = CASCADE
    ),
    ForeignKey(
      entity = MetadataEntity::class,
      parentColumns = ["metadataId"],
      childColumns = ["metadataId"],
      onDelete = CASCADE
    )
  ]
)
data class BookmarkMetadataCrossRef(
  val bookmarkId: Long,
  val metadataId: Long
)
