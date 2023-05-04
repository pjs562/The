package unist.pjs.the.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LikeBoard(
    @PrimaryKey
    val id: String,
    val isLeft: String = ""
)
