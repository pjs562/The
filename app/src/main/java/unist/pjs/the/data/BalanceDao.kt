package unist.pjs.the.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BalanceDao {
    @Query("SELECT * FROM Balance")
    fun getBalanceList(): Flow<List<Balance>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllBalance(items: List<Balance>)

    @Query("SELECT * FROM Balance WHERE _id = :id")
    suspend fun getBalance(id: String): Balance

    @Update
    suspend fun updateBalance(balance: Balance)

    @Query("DELETE FROM Balance")
    suspend fun deleteAll()
}