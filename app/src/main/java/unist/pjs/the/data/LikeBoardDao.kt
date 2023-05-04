package unist.pjs.the.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LikeBoardDao {
    @Query("SELECT * FROM LikeBoard WHERE id = :id")
    suspend fun getLikeBoard(id: String): LikeBoard

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLikeBoards(items: List<LikeBoard>)

    @Insert
    suspend fun insertLikeBoard(item: LikeBoard)

    @Query("DELETE FROM LikeBoard")
    suspend fun deleteAllLikeBoard()
}