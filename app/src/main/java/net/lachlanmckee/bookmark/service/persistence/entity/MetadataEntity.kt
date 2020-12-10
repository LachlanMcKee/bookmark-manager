package net.lachlanmckee.bookmark.service.persistence.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "metadata")
data class MetadataEntity(
  @PrimaryKey(autoGenerate = true) val metadataId: Long,
  val name: String
) {
  constructor(name: String) : this(0, name)
}
