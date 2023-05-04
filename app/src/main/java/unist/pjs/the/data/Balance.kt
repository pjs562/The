package unist.pjs.the.data

import androidx.room.Entity
import androidx.room.PrimaryKey

data class BalanceList(
    val balances: List<Balance>,
)

@Entity
data class Balance(
    @PrimaryKey
    val _id: String,
    val anonymous: Boolean,
    val left: String,
    val right: String,
    val leftCount: String,
    val rightCount: String,
    val name: String,
    val replies: List<Reply>?,
    val createdAt: String,
    val updatedAt: String,
)
