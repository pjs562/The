package unist.pjs.the.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {
    @Query("SELECT * FROM Room")
    suspend fun getRoomList(): List<Room>

    @Query("SELECT * FROM Room")
    fun getRoom(): Flow<List<Room>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRoom(items: List<Room>)

    @Query("SELECT * FROM Room WHERE couple = :couple")
    suspend fun getRoom(couple: List<String>): Room?

    @Query("DELETE FROM Room")
    suspend fun deleteRoom()
}