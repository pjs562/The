package unist.pjs.the.data

import com.google.gson.annotations.SerializedName

data class RequestAuth(
    @SerializedName("name")
    val name: String,
    @SerializedName("auth_no")
    val auth_no: String
)

data class RequestBody(
    @SerializedName("name")
    val name: String,

    @SerializedName("boardId")
    val boardId: String,

    @SerializedName("replyId")
    val replyId: String
)