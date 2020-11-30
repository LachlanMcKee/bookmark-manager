package net.lachlanmckee.bookmark.service.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    val name: String,
    val link: String,
    val folderId: Int?
) {
    constructor(name: String, link: String, folderId: Int?) : this(0, name, link, folderId)
}
