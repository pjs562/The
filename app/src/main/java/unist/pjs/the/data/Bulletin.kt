package unist.pjs.the.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Bulletin(
    @PrimaryKey
    val _id: String,
    val name: String,
    val title: String,
    val anonymous: Boolean,
    val content: String,
    val like: Int,
    @SerializedName("replies")
    var replies: List<Reply>?,
    val createdAt: String,
    val updatedAt: String,
)

data class BoardList(
    val boards: List<Bulletin>,
)

data class Reply(
    val _id: String,
    val anonymous: Boolean,
    val content: String,
    val name: String,
    val createdAt: String,
    val updatedAt: String,
)