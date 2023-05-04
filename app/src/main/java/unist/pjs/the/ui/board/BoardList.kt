package unist.pjs.the.ui.board

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import unist.pjs.the.R
import unist.pjs.the.data.*
import unist.pjs.the.extends.Preferences
import unist.pjs.the.extends.convertDateToTimestamp
import unist.pjs.the.extends.convertTimestampToDate2
import unist.pjs.the.extends.timeDiff

@Composable
fun BoardBalanceList(
    viewModel: BalanceViewModel,
    onNavigate: (NavDirections) -> Unit,
) {
    val list by viewModel.balanceList.observeAsState()
    list?.let { items ->
        SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = false), onRefresh = {
            viewModel.getBalanceList("")
        }) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            ) {
                val lastIndex = items.lastIndex
                itemsIndexed(items, key = { _, item -> item._id }) { i, balance ->
                    if (i == lastIndex) {
                        viewModel.getBalanceList(balance._id)
                    }
                    Balance(balance, onNavigate)
                    Divider(modifier = Modifier.padding(top = 5.dp),
                        color = MaterialTheme.colors.onSurface)
                }
            }
        }
    }
}

@Composable
fun Balance(balance: Balance, onNavigate: (NavDirections) -> Unit) {
    val action = BalanceFragmentDirections.actionBalanceFragmentToBalanceDetailFragment(balance._id)
    TextButton(onClick = { onNavigate(action) }) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
                text = balance.left, color = MaterialTheme.colors.primary,
                fontSize = 16.sp,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center)
            Column(modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "VS",
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                )
                Text(text = balance.createdAt.convertDateToTimestamp()
                    .timeDiff() + "  |  " + if (balance.anonymous) "익명" else balance.name,
                    color = MaterialTheme.colors.secondaryVariant,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 12.dp))
            }
            Text(modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
                text = balance.right,
                color = MaterialTheme.colors.primary,
                fontSize = 16.sp,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun BoardList(
    type: String,
    boardListViewModel: BoardListViewModel,
    onNavigate: (NavDirections) -> Unit,
) {
    val list by boardListViewModel.bulletinList.observeAsState()
    list?.let { items ->
        BoardDetailList(type, boardListViewModel, items, onNavigate)
    }
}

@Composable
fun BoardDetailList(
    type: String,
    viewModel: BoardListViewModel,
    bulletins: List<Bulletin>,
    onNavigate: (NavDirections) -> Unit,
) {
    SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = false), onRefresh = {
        viewModel.getBoardList("")
    }) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        ) {
            val lastIndex = bulletins.lastIndex
            itemsIndexed(bulletins, key = { _, item -> item._id }) { i, bulletin ->
                if (i == lastIndex) {
                    viewModel.getBoardList(bulletin._id)
                }
                Bulletin(type, bulletin, onNavigate)
                Divider(modifier = Modifier.padding(top = 5.dp),
                    color = MaterialTheme.colors.onSurface)
            }
        }
    }
}

@Composable
fun Bulletin(type: String, item: Bulletin, onNavigate: (NavDirections) -> Unit) {
    val action =
        BoardListFragmentDirections.actionBoardListFragmentToBoardDetailFragment(type, item._id)

    TextButton(onClick = { onNavigate(action) }) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = item.title, color = MaterialTheme.colors.primary,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold)

            Text(text = item.content,
                color = MaterialTheme.colors.secondaryVariant,
                fontWeight = FontWeight.Light,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 16.sp)

            Text(text = item.createdAt.convertDateToTimestamp()
                .timeDiff() + "  |  " + if (item.anonymous) "익명" else item.name,
                color = MaterialTheme.colors.secondaryVariant,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 12.dp))
        }
    }
}

@Composable
fun CommentBalanceList(viewModel: BalanceDetailViewModel) {
    val balance by viewModel.balance.observeAsState()
    val profileList by viewModel.profileList.observeAsState()
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = Modifier.padding(start = 24.dp, end = 24.dp)
    ) {
        item {
            balance?.let { CommentBalanceHeader(it, viewModel) }
        }
        balance?.replies?.let {
            items(it, key = { item -> item._id }) { item ->
                CommentBalance(item, profileList, viewModel)
                Divider(color = MaterialTheme.colors.onSurface)
            }
        }
    }
}

@Composable
fun CommentBalanceHeader(balance: Balance, viewModel: BalanceDetailViewModel) {
    val profile by viewModel.profile.observeAsState()
    val isLeft by viewModel.isLeft.observeAsState()
    val isLike by viewModel.isLike.observeAsState()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 0.dp, bottom = 20.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(painter = rememberAsyncImagePainter(model = if (balance.anonymous) "" else "https://api.theloveyouth.com/${profile?.thumbnail}",
                error = if (profile?.gender == "남자") painterResource(id = R.drawable.ic_men)
                else painterResource(id = R.drawable.ic_girl)),
                contentDescription = null,
                modifier = Modifier
                    .width(45.dp)
                    .height(45.dp)
                    .clip(CircleShape))

            Spacer(Modifier.size(10.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(
                        text = if (balance.anonymous) "익명" else balance.name,
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp)
                    Text(
                        text = balance.createdAt.convertDateToTimestamp().convertTimestampToDate2(),
                        color = MaterialTheme.colors.secondaryVariant,
                        fontSize = 12.sp)
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally) {
                TextButton(onClick = {
                    if (isLike != true)
                        viewModel.postLike(balance._id, true)
                }) {
                    Text(modifier = Modifier
                        .background(
                            shape = AbsoluteRoundedCornerShape(20.dp),
                            color = if (isLike == true) {
                                if (isLeft == "true") Color(0xFF9FED6f) else Color(0xFF515350)
                            } else MaterialTheme.colors.onSurface
                        )
                        .padding(horizontal = 13.dp, vertical = 7.dp),
                        text = balance.left, color = MaterialTheme.colors.primary,
                        fontSize = 16.sp,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center)
                }
                Text(text = balance.leftCount,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center)
            }
            Spacer(modifier = Modifier.size(20.dp))

            Text(
                text = "VS",
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
            Spacer(modifier = Modifier.size(20.dp))

            Column(modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally) {
                TextButton(onClick = {
                    if (isLike != true)
                        viewModel.postLike(balance._id, false)
                }) {
                    Text(modifier = Modifier
                        .background(
                            shape = AbsoluteRoundedCornerShape(20.dp),
                            color = if (isLike == true) {
                                if (isLeft == "false") Color(0xFF9FED6f) else Color(0xFF515350)
                            } else MaterialTheme.colors.onSurface
                        )
                        .padding(horizontal = 13.dp, vertical = 7.dp),
                        text = balance.right, color = MaterialTheme.colors.primary,
                        fontSize = 16.sp,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center)
                }

                Text(text = balance.rightCount,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center)
            }
        }
        Spacer(Modifier.size(30.dp))

        Divider(color = MaterialTheme.colors.onSurface)
    }
}

@Composable
fun CommentList(viewModel: BoardDetailViewModel) {
    val bulletin by viewModel.bulletin.observeAsState()
    val profileList by viewModel.profileList.observeAsState()
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = Modifier.padding(start = 24.dp, end = 24.dp)
    ) {
        item {
            bulletin?.let { CommentHeader(it, viewModel) }
        }
        bulletin?.replies?.let {
            items(it, key = { item -> item._id }) { item ->
                Comment(item, profileList, viewModel)
                Divider(color = MaterialTheme.colors.onSurface)
            }
        }
    }
}

@Composable
fun CommentBalance(reply: Reply, profileList: List<Profile>?, viewModel: BalanceDetailViewModel) {
    val profile = profileList?.find { it.name == reply.name }

    Row(modifier = Modifier.fillMaxWidth()) {
        Image(painter =
        if (profile?.gender == "남자") painterResource(id = R.drawable.ic_men)
        else painterResource(id = R.drawable.ic_girl),
            contentDescription = null,
            modifier = Modifier
                .width(45.dp)
                .height(45.dp)
                .clip(CircleShape))

        Spacer(modifier = Modifier.size(7.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(Modifier.fillMaxWidth(0.86f)) {
                Text(
                    text = if (reply.anonymous) "익명" else reply.name,
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp)

                Text(
                    text = reply.content,
                    color = MaterialTheme.colors.secondaryVariant,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp)

                Spacer(modifier = Modifier.size(5.dp))

                Text(text = reply.createdAt.convertDateToTimestamp().convertTimestampToDate2(),
                    color = MaterialTheme.colors.secondaryVariant,
                    fontWeight = FontWeight.Medium,
                    fontSize = 10.sp)

                Spacer(modifier = Modifier.size(10.dp))
            }
            if (Preferences.userName == reply.name) {
                IconButton(onClick = { viewModel.deleteReply(reply.name, reply._id) }) {
                    Icon(painter = painterResource(id = R.drawable.ic_trash),
                        contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun CommentHeader(bulletin: Bulletin, viewModel: BoardDetailViewModel) {
    val isLike by viewModel.isLike.observeAsState()
    val profile by viewModel.profile.observeAsState()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 0.dp, bottom = 20.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(painter = rememberAsyncImagePainter(model = if (bulletin.anonymous) "" else "https://api.theloveyouth.com/${profile?.thumbnail}",
                error = if (profile?.gender == "남자") painterResource(id = R.drawable.ic_men)
                else painterResource(id = R.drawable.ic_girl)),
                contentDescription = null,
                modifier = Modifier
                    .width(45.dp)
                    .height(45.dp)
                    .clip(CircleShape))

            Spacer(Modifier.size(10.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(
                        text = if (bulletin.anonymous) "익명" else bulletin.name,
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp)
                    Text(
                        text = bulletin.createdAt.convertDateToTimestamp()
                            .convertTimestampToDate2(),
                        color = MaterialTheme.colors.secondaryVariant,
                        fontSize = 12.sp)
                }

                Row(modifier = Modifier
                    .background(
                        shape = AbsoluteRoundedCornerShape(20.dp),
                        color = MaterialTheme.colors.onSurface
                    )
                    .padding(horizontal = 10.dp, vertical = 5.dp)) {
                    Icon(painter = painterResource(id = R.drawable.ic_good),
                        contentDescription = null)
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(text = "+ ${bulletin.like}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.ExtraBold)
                }
            }
        }
        Text(modifier = Modifier.fillMaxWidth(),
            text = bulletin.title,
            fontSize = 16.sp,
            color = MaterialTheme.colors.primary,
            fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.size(20.dp))
        Text(modifier = Modifier.fillMaxWidth(),
            text = bulletin.content,
            fontSize = 16.sp,
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colors.secondaryVariant)
        IconButton(modifier = Modifier
            .padding(vertical = 40.dp)
            .size(50.dp)
            .background(color = MaterialTheme.colors.primary,
                shape = CircleShape),
            onClick = {
                if (isLike != true)
                    viewModel.postLike(bulletin._id)
            }) {
            Icon(painter = if (isLike == true) painterResource(id = R.drawable.ic_good_full) else painterResource(
                id = R.drawable.ic_good_white),
                tint = Color.White,
                contentDescription = null)
        }
        Divider(color = MaterialTheme.colors.onSurface)
    }
}

@Composable
fun Comment(reply: Reply, profileList: List<Profile>?, viewModel: BoardDetailViewModel) {
    val profile = profileList?.find { it.name == reply.name }

    Row(modifier = Modifier.fillMaxWidth()) {
        Image(painter = if (reply.anonymous) {
            if (profile?.gender == "남자") painterResource(id = R.drawable.ic_men)
            else painterResource(id = R.drawable.ic_girl)
        } else rememberAsyncImagePainter(model = "https://api.theloveyouth.com/${profile?.thumbnail}", error =  if (profile?.gender == "남자") painterResource(id = R.drawable.ic_men)
        else painterResource(id = R.drawable.ic_girl)),
            contentDescription = null,
            modifier = Modifier
                .width(45.dp)
                .height(45.dp)
                .clip(CircleShape))

        Spacer(modifier = Modifier.size(7.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(Modifier.fillMaxWidth(0.86f)) {
                Text(
                    text = if (reply.anonymous) "익명" else reply.name,
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp)

                Text(
                    text = reply.content,
                    color = MaterialTheme.colors.secondaryVariant,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp)

                Spacer(modifier = Modifier.size(5.dp))

                Text(text = reply.createdAt.convertDateToTimestamp().convertTimestampToDate2(),
                    color = MaterialTheme.colors.secondaryVariant,
                    fontWeight = FontWeight.Medium,
                    fontSize = 10.sp)

                Spacer(modifier = Modifier.size(10.dp))
            }

            if (Preferences.userName == reply.name) {
                IconButton(onClick = { viewModel.deleteReply(reply.name, reply._id) }) {
                    Icon(painter = painterResource(id = R.drawable.ic_trash),
                        contentDescription = null)
                }
            }
        }
    }
}