package unist.pjs.the.ui.message

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDirections
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import unist.pjs.the.R
import unist.pjs.the.data.Chat
import unist.pjs.the.data.ChatRoom
import unist.pjs.the.data.Profile
import unist.pjs.the.extends.Preferences

@Composable
fun ChatRoomList(
    viewModel: MessageViewModel,
    onNavigate: (NavDirections) -> Unit,
) {
    val list by viewModel.chatRoomList.observeAsState()
    val profileList by viewModel.profileList.observeAsState()
    list?.let { chatRoomList ->
        LazyColumn(verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.padding(start = 18.dp, end = 18.dp, top = 20.dp)) {
            items(chatRoomList) {
                ChatRoom(item = it, profileList = profileList, onNavigate = onNavigate)
            }
        }
    }
}

@Composable
fun ChatRoom(item: ChatRoom, profileList: List<Profile>?, onNavigate: (NavDirections) -> Unit) {
    val profile = profileList?.find { it.name == item.name }
    val action = MessageFragmentDirections.actionNavigationMessageToMessageDetailFragment(item.id)

    TextButton(onClick = { onNavigate(action) }) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Image(painter = rememberAsyncImagePainter(model = "https://api.theloveyouth.com/${profile?.thumbnail}",
                error = if (profile?.gender == "남자") painterResource(id = R.drawable.ic_men)
                else painterResource(id = R.drawable.ic_girl)),
                contentDescription = null,
                modifier = Modifier
                    .width(58.dp)
                    .height(58.dp)
                    .clip(CircleShape))

            Spacer(modifier = Modifier.size(10.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Column(Modifier.padding(top = 5.dp)) {
                    Text(text = item.name,
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        fontSize = 14.sp,
                        overflow = TextOverflow.Ellipsis)
                    Spacer(modifier = Modifier.size(5.dp))
                    Text(
                        text = item.content,
                        color = MaterialTheme.colors.secondaryVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.size(5.dp))

                Column(modifier = Modifier.padding(top = 5.dp),
                    horizontalAlignment = Alignment.End) {
                    if(item.time.isNotEmpty()){
                        Text(text = item.time,
                            color = MaterialTheme.colors.secondaryVariant,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.size(7.dp))
                    if (item.unread != 0L) {
                        Box(contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(23.dp)
                                .background(color = Color(0xFFFF7051), shape = CircleShape)) {
                            Text(
                                modifier = Modifier.padding(end = 1.dp),
                                text = item.unread.toString(),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun ChatList(
    viewModel: MessageDetailViewModel,
) {
    val list by viewModel.chatList.observeAsState()
    val profile by viewModel.profile.observeAsState()
    val position by viewModel.position.observeAsState()
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    LaunchedEffect(key1 = position) {
        coroutineScope.launch {
            position?.let { listState.scrollToItem(it) }
        }
    }

    list?.let { chatRoomList ->
        LazyColumn(verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.padding(start = 7.dp, end = 7.dp),
            state = listState) {

            items(chatRoomList) {item ->
                if (item.person == Preferences.userName) {
                    ChatMe(item = item)
                } else {
                    ChatYou(item = item, profile = profile)
                }
            }
        }
    }
}

@Composable
fun ChatMe(item: Chat) {
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Bottom) {
        Text(textAlign = TextAlign.Center,
            text = item.time,
            color = MaterialTheme.colors.primary,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp)

        Spacer(modifier = Modifier.size(3.dp))

        Box(modifier = Modifier
            .background(color = Color(0xFFEAEAEA), shape = AbsoluteRoundedCornerShape(20.dp))) {
            Text(modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp),
                textAlign = TextAlign.Center,
                text = item.content,
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp)
        }
    }
}

@Composable
fun ChatYou(item: Chat, profile: Profile?) {
    Row(verticalAlignment = Alignment.Bottom) {
        Image(painter = rememberAsyncImagePainter(model = "https://api.theloveyouth.com/${profile?.thumbnail}",
            error = if (profile?.gender == "남자") painterResource(id = R.drawable.ic_men)
            else painterResource(id = R.drawable.ic_girl)),
            contentDescription = null,
            modifier = Modifier
                .width(45.dp)
                .height(45.dp)
                .clip(CircleShape))

        Spacer(modifier = Modifier.size(5.dp))
        Column() {
            Text(modifier = Modifier
                .padding(bottom = 2.dp),
                textAlign = TextAlign.Center,
                text = item.person,
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp)

            Box(modifier = Modifier
                .background(color = Color(0xFFEAEAEA), shape = AbsoluteRoundedCornerShape(20.dp))) {
                Text(modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                    textAlign = TextAlign.Center,
                    text = item.content,
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp)
            }
        }

        Spacer(modifier = Modifier.size(3.dp))

        Text(textAlign = TextAlign.Center,
            text = item.time,
            color = MaterialTheme.colors.primary,
            fontWeight = FontWeight.Normal,
            fontSize = 10.sp)
    }
}