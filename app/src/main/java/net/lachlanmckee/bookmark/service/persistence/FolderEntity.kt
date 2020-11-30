package net.lachlanmckee.bookmark.service.persistence

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "folder")
data class FolderEntity(
    @PrimaryKey val uid: Int,
    var parentId: Int?,
    var name: String
)
