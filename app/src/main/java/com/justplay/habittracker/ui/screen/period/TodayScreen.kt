package com.justplay.habittracker.ui.screen.period

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.justplay.habittracker.R
import com.justplay.habittracker.ui.theme.AppTypography
import com.justplay.habittracker.ui.view.DraggableItemWithActions

data class HabitUi(
    val color: Color,
    val title: Int,
    val icon: Int
)

@Composable
fun TodayScreen() {
    /**
     * TODO Replace with database
     */
    val habits = remember {
        mutableStateListOf(
            HabitUi(
                color = Color("#FC9CA0".toColorInt()),
                title = R.string.ex_habit_list_1,
                icon = R.mipmap.vec_bulls_eye
            ),
            HabitUi(
                color = Color("#CCCCFB".toColorInt()),
                title = R.string.ex_habit_list_2,
                icon = R.mipmap.vec_trophy
            ),
            HabitUi(
                color = Color("#D0FCCC".toColorInt()) ,
                title = R.string.ex_habit_list_3
                , icon = R.mipmap.vec_smile_face_with_halo
            )
        ).toTypedArray()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        itemsIndexed(
            items = habits,
            key = { _, habit -> habit.title }
        ) { _, habit ->
            DraggableItemWithActions {
                HabitListBaseItem(color = habit.color,
                    habit.title,
                    habit.icon,
                    modifier = it
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

val ListItemHeight = 68.dp

@Composable
fun HabitListBaseItem(
    color: Color,
    @StringRes headlineText: Int,
    @DrawableRes leadingIcon: Int,
    modifier: Modifier = Modifier
) {
    ListItem(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .height(ListItemHeight),
        colors = ListItemDefaults.colors(
            containerColor = color
        ),
        headlineContent = {
            Text(
                text = stringResource(headlineText),
                style = AppTypography.titleMedium
            )
        },
        leadingContent = {
            Image(
                painter = painterResource(leadingIcon),
                contentDescription = "Image"
            )
        }
    )
}

@Preview
@Composable
fun TodayScreenPreview() {
    TodayScreen()
}

