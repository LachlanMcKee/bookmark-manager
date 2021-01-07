package net.lachlanmckee.bookmark.service.persistence.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class BookmarkWithMetadata(
  @Embedded val bookmark: BookmarkEntity,
  @Relation(
    parentColumn = "bookmarkId",
    entityColumn = "metadataId",
    associateBy = Junction(BookmarkMetadataCrossRef::class)
  )
  val metadata: List<MetadataEntity>
)
