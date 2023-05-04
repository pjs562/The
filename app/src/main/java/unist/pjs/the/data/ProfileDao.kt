package unist.pjs.the.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProfileDao {
    @Query("SELECT * FROM Profile WHERE name = :name")
    suspend fun getProfile(name: String): Profile

    @Query("SELECT * FROM Profile")
    suspend fun getProfileList(): List<Profile>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfiles(items: List<Profile>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(items: Profile)

    @Query("DELETE FROM Profile")
    suspend fun deleteAll()
}