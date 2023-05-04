package unist.pjs.the.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BulletinDao {
    @Query("SELECT * FROM Bulletin")
    fun getBulletin(): Flow<List<Bulletin>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllBulletin(items: List<Bulletin>)

    @Query("SELECT * FROM Bulletin WHERE _id = :id")
    suspend fun getBulletin(id: String): Bulletin

    @Update
    suspend fun updateBulletin(bulletin: Bulletin)

    @Query("DELETE FROM Bulletin")
    suspend fun deleteAll()
}