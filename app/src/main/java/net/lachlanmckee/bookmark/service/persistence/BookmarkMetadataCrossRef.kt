package net.lachlanmckee.bookmark.service.persistence

import androidx.room.Entity

@Entity(primaryKeys = ["bookmarkId", "metadataId"])
data class BookmarkMetadataCrossRef(
    val bookmarkId: Long,
    val metadataId: Long
)
