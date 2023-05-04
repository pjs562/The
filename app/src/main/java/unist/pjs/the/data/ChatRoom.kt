package unist.pjs.the.data

data class ChatRoom(
    val id: String,
    val name: String,
    val content: String,
    val unread: Long,
    val time: String
)

data class Chat(
    val person: String,
    val time: String,
    val content: String
)
