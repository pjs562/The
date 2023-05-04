package unist.pjs.the.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Match(
    @PrimaryKey
    val _id: String,
    val isHeart: Boolean,
    @SerializedName("to")
    val name: String,
    val createdAt: String,
    val updatedAt: String
)
