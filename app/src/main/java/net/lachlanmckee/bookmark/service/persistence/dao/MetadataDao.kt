package net.lachlanmckee.bookmark.service.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import net.lachlanmckee.bookmark.service.persistence.entity.MetadataEntity

@Dao
interface MetadataDao {
  @Query("SELECT * FROM metadata")
  fun getAllMetadata(): Flow<List<MetadataEntity>>

  @Insert
  suspend fun insertAll(vararg metadataEntities: MetadataEntity): List<Long>

  @Query("DELETE FROM metadata")
  suspend fun deleteAll()
}
