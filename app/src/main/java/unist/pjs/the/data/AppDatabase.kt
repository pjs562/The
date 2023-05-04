package unist.pjs.the.data


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson


@Database(entities = [Room::class, LikeBoard::class, Profile::class, Bulletin::class, Match::class, GroupName::class, Balance::class], version = 1, exportSchema = false)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao

    abstract fun bulletinDao(): BulletinDao

    abstract fun matchDao(): MatchDao

    abstract fun groupNameDao(): GroupNameDao

    abstract fun likeBoardDao(): LikeBoardDao

    abstract fun balanceDao():BalanceDao

    abstract fun roomDao():RoomDao
}

class Converters {
    @TypeConverter
    fun listToJson(list: List<String>?) = list?.let { Gson().toJson(list) }

    @TypeConverter
    fun jsonToList(value: String?) =
        value?.let { Gson().fromJson(value, Array<String>::class.java).toList() }

    @TypeConverter
    fun replyListToJson(list: List<Reply>?) = list?.let { Gson().toJson(list) }

    @TypeConverter
    fun jsonToReplyList(value: String?) =
        value?.let { Gson().fromJson(value, Array<Reply>::class.java).toList() }
}