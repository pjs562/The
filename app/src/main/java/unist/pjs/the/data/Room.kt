package unist.pjs.the.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Room(
    @PrimaryKey
    val _id: String,
    val couple: List<String>,
    val roomid: String,
    val ctype: String,
    val createdAt: String,
    val updatedAt: String
)

