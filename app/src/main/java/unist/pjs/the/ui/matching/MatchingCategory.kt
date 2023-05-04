package unist.pjs.the.ui.matching

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDirections
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import unist.pjs.the.R
import unist.pjs.the.data.GroupName
import unist.pjs.the.data.Profile

@Composable
fun MatchingCategory(matchingViewModel: MatchingViewModel) {
    val list by matchingViewModel.groupNameList.observeAsState()
    list?.let { items ->
        if (items.isNotEmpty()) {
            if (matchingViewModel.type.value == true) {
                MatchingAgeList(
                    items = items,
                    viewModel = matchingViewModel
                )
            } else {
                MatchingCellList(
                    items = items,
                    viewModel = matchingViewModel
                )
            }
        }
    }
}

@Composable
fun MatchingAgeList(items: List<GroupName>, viewModel: MatchingViewModel) {
    val (selectedOption, onOptionSelected) = remember {
        mutableStateOf(
            items[0]
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp),
        contentAlignment = Alignment.Center,

        ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items.forEachIndexed { index, groupName ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(65.dp)
                        .selectable(selected = groupName == selectedOption) {
                            onOptionSelected(groupName)
                            viewModel.titleIndexList.value?.let {
                                if (it.size > index) {
                                    viewModel.changePosition(it[index])
                                }
                            }
                        }
                        .background(
                            color = if (groupName == selectedOption) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant,
                            shape = CircleShape
                        )
                ) {
                    Text(
                        text = groupName.name,
                        color = if (groupName == selectedOption) MaterialTheme.colors.onPrimary else MaterialTheme.colors.secondary,
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun MatchingCellList(items: List<GroupName>, viewModel: MatchingViewModel) {
    val (selectedOption, onOptionSelected) = remember {
        mutableStateOf(
            items[0]
        )
    }

    LazyVerticalGrid(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        columns = GridCells.Fixed(5),
        modifier = Modifier
            .padding(bottom = 10.dp, start = 16.dp, end = 16.dp)
            .size(190.dp)
    ) {
        itemsIndexed(items, key = {_, item ->  item.name}) { index, groupName ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(CircleShape)
                    .width(50.dp)
                    .height(52.dp)
                    .selectable(selected = groupName == selectedOption) {
                        onOptionSelected(groupName)
                        viewModel.titleIndexList.value?.let {
                            viewModel.changePosition(it[index])
                        }
                    }
                    .background(
                        color = if (groupName == selectedOption) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant,
                        shape = CircleShape
                    )
            ) {
                Text(
                    text = groupName.name,
                    color = if (groupName == selectedOption) MaterialTheme.colors.onPrimary else MaterialTheme.colors.secondary,
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun MatchingList(viewModel: MatchingViewModel, onNavigate: (NavDirections) -> Unit) {
    val type = remember { mutableStateOf(viewModel.type.value) }
    val list by viewModel.groupList.observeAsState()
    val position by viewModel.position.observeAsState()
    val listState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(key1 = position) {
        coroutineScope.launch {
            position?.let { listState.scrollToItem(it) }
        }
    }
    var count = 0
    val titleIndexList: MutableList<Int> = arrayListOf()

    list?.forEach { group ->
        titleIndexList.add(count++)
        count += group.members?.size ?: 0
    }.also {
        viewModel.setTitleIndexList(titleIndexList)
    }

    Surface(
        elevation = 10.dp,
        shape = AbsoluteRoundedCornerShape(topLeft = 40.dp),
        modifier = Modifier.padding(top = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = false), onRefresh = {
                viewModel.type.value?.let { viewModel.initPeoples2(it) }
            }) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    state = listState,
                ) {
                    list?.forEach { Group ->
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            TitleText(Group.grouping)
                        }
                        items(Group.members!!, key = {item -> item.name}) { item ->
                            ProfileImage(profile = item, onNavigate)
                        }
                    }
                }
            }
            var visible by remember {
                mutableStateOf(false)
            }

            Box(
                modifier = Modifier
                    .width(150.dp)
                    .align(Alignment.TopEnd)
                    .padding(top = 16.dp)
            ) {

                AnimatedVisibility(
                    visible = visible,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .background(
                                shape = AbsoluteRoundedCornerShape(
                                    bottomLeft = 10.dp,
                                    bottomRight = 10.dp
                                ), color = MaterialTheme.colors.onPrimary.copy(alpha = 0.8f)
                            )
                    ) {
                        TextButton(modifier = Modifier
                            .padding(
                                top = 10.dp
                            )
                            .fillMaxWidth(),
                            onClick = {
                                visible = !visible
                                viewModel.type.value?.let {
                                    viewModel.onTypeChanged(true)
                                    type.value = true
                                }
                            }) {
                            Text(text = "나이별로 보기")
                        }
                        Divider(
                            modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                            color = Color.Gray.copy(alpha = 0.5f)
                        )
                        TextButton(modifier = Modifier
                            .fillMaxWidth(),
                            onClick = {
                                visible = !visible
                                viewModel.type.value?.let {
                                    viewModel.onTypeChanged(false)
                                    type.value = false
                                }
                            }) {
                            Text(text = "목장별로 보기")
                        }
                    }
                }

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = AbsoluteRoundedCornerShape(30.dp),
                    onClick = {
                        visible = !visible
                    }) {
                    Text(text = if (type.value == true) "나이별로 보기" else "목장별로 보기")
                    Icon(
                        painter = if (visible) painterResource(id = R.drawable.ic_baseline_expand_more_24) else painterResource(
                            id = R.drawable.ic_baseline_expand_less_24
                        ),
                        contentDescription = null
                    )
                }
            }
        }
    }


}


@Composable
fun ProfileImage(profile: Profile, onNavigate: (NavDirections) -> Unit) {
    val action =
        MatchingFragmentDirections.actionNavigationMatchingToNavigationProfileDetail(name = profile.name,
            age = profile.age.toString(),
            cellGroup = profile.group,
            gender = profile.gender,
            bio = profile.bio ?: "",
            imageUrl = if (profile.imageUrls?.size!! > 0) profile.imageUrls[0] else "")

    Column(modifier = Modifier.clickable { onNavigate(action) },
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = rememberAsyncImagePainter("https://api.theloveyouth.com/${profile.thumbnail}",
                error = painterResource(id = if (profile.gender == "남자") R.drawable.ic_men else R.drawable.ic_girl)),
            contentDescription = null,
            modifier = Modifier
                .width(75.dp)
                .height(75.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = profile.name,
            color = MaterialTheme.colors.onSecondary,
            style = MaterialTheme.typography.body2
        )
    }
}

@Composable
fun TitleText(age: String) {
    Text(
        text = age,
        color = MaterialTheme.colors.primary,
        style = MaterialTheme.typography.body1,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
    )
}