package unist.pjs.the.data

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Peoples(
    val peoples: List<Group>
)

data class Group(
    val grouping: String,
    val members: List<Profile>?
)

@Entity
data class Profile(
    @PrimaryKey
    val _id: String,
    val name: String,
    val gender: String,
    val phone: String,
    val group: String,
    val heartCount: Int,
    val likeCount: Int,
    val admin: Boolean,
    val imageUrls: List<String>?,
    val bio: String? = "",
    val age: Int,
    val thumbnail: String? = ""
)