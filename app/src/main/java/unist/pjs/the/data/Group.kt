package unist.pjs.the.data

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Groups(
    val groups: List<GroupName>
)

@Entity
data class GroupName(
    @PrimaryKey
    val _id: String,
    val name: String
)
