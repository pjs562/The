package unist.pjs.the.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import unist.pjs.the.data.GroupName

@Composable
fun GridCellList(cellList: List<GroupName>, viewModel: SignupFragment2ViewModel) {

    cellList.let {
        val (selectedOption, onOptionSelected) = remember {
            mutableStateOf(
                GroupName("","")
            )
        }
        LazyVerticalGrid(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp)
                .size(190.dp)
        ) {
            items(it) { group ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(CircleShape)
                        .width(50.dp)
                        .height(65.dp)
                        .selectable(selected = group == selectedOption) {
                            onOptionSelected(group)
                            viewModel.setGroup(group.name)
                        }
                        .background(
                            color = if (group == selectedOption) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant,
                            shape = CircleShape
                        )
                ) {
                    Text(
                        text = group.name,
                        color = if (group == selectedOption) MaterialTheme.colors.onPrimary else MaterialTheme.colors.secondary,
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}