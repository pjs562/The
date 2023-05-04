package unist.pjs.the.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
@Dao
interface MatchDao {
    @Query("SELECT * FROM `match`")
    fun getMatches(): Flow<List<Match>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMatch(items: List<Match>)

    @Insert
    suspend fun insertMatch(match: Match)

    @Query("SELECT * FROM `match` WHERE name = :name")
    suspend fun getMatch(name: String): Match

    @Query("DELETE FROM `match`")
    suspend fun deleteMatch()
}