package net.lachlanmckee.bookmark.service.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "metadata")
data class MetadataEntity(
  @PrimaryKey(autoGenerate = true) val metadataId: Int,
  val name: String
) {
  constructor(name: String) : this(0, name)
}
