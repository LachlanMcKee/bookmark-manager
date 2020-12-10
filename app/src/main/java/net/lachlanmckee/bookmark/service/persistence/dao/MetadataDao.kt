package net.lachlanmckee.bookmark.service.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import net.lachlanmckee.bookmark.service.persistence.entity.MetadataEntity

@Dao
interface MetadataDao {
  @Insert
  suspend fun insertAll(vararg metadataEntities: MetadataEntity): List<Long>

  @Query("DELETE FROM metadata")
  suspend fun deleteAll()
}
