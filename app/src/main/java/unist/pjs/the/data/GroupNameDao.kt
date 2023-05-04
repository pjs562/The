package unist.pjs.the.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupNameDao {
    @Query("SELECT * FROM groupname")
    fun getGroupNameList(): Flow<List<GroupName>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllGroupName(items: List<GroupName>)

    @Query("DELETE FROM groupName")
    suspend fun deleteAllGroupName()
}