package net.lachlanmckee.bookmark.service.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val link: String
){
    constructor(name: String, link: String) : this(0, name, link)
}
