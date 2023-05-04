package unist.pjs.the.data

import com.google.gson.annotations.SerializedName

data class UserInfo(
    val _id: String,
    val name: String?,
    val birth: String,
    val gender: String,
    val phone: String,
    val group: String,
    val heartCount: Int,
    val likeCount: Int,
    val imageUrls: List<String>?,
    val age: String,
    val bio: String?,
    val admin: String,
    val thumbnail: String?,
    val remainHeart: Int,
    val remainLike: Int,
    val matches: List<Match>,
    val likedBoards: List<String>?,
    val rooms: List<Room>?,
    @SerializedName("error")
    val error: String? = "",
    @SerializedName("ok")
    val ok: String? = "",
)