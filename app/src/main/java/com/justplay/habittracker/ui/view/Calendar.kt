package com.justplay.habittracker.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.justplay.habittracker.data.DayUi
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

/**
 * 產生一個月要顯示的 6*7 日期格（含前後月的補格）
 */
fun buildMonthDays(
    yearMonth: YearMonth,
    disableByDate: Boolean,
    selectedDaysOfMonth: Set<Int>
): List<DayUi> {
    val firstOfMonth = yearMonth.atDay(1)
    val firstDayOfWeek = firstOfMonth.dayOfWeek
    val daysInMonth = yearMonth.lengthOfMonth()
    val today = LocalDate.now()

    /**
     * 這個月第一天是星期幾（以 Sunday=0 ... Saturday=6）
     */
    val offset = (firstDayOfWeek.value % 7)

    /**
     * 六個星期 * 七天 = 42 格
     */
    val totalCells = 42
    val days = mutableListOf<DayUi>()

    // 前一個月
    val prevMonth = yearMonth.minusMonths(1)
    val daysInPrevMonth = prevMonth.lengthOfMonth()

    // 前一個月的尾巴
    for (i in offset - 1 downTo 0) {
        val day = daysInPrevMonth - i
        val date = prevMonth.atDay(day)
        days += DayUi(
            date = date,
            selected = false,
            enabled = false
        )
    }

    // 本月
    for (day in 1..daysInMonth) {
        val date = yearMonth.atDay(day)
        val enabled = !date.isBefore(today)
        days += DayUi(
            date = date,
            selected = day in selectedDaysOfMonth,
            enabled = if (!disableByDate) true else enabled
        )
    }

    // 下個月
    val nextMonth = yearMonth.plusMonths(1)
    var nextDay = 1
    while (days.size < totalCells) {
        days += DayUi(
            date = nextMonth.atDay(nextDay++),
            selected = false,
            enabled = false
        )
    }

    return days
}

@Composable
fun DateCalendar(
    modifier: Modifier = Modifier,
    initYearMonth: YearMonth = YearMonth.now(),
    locale: Locale = Locale.US,
    onSelectionChanged: (LocalDate) -> Unit
) {
    val yearMonthSaver = Saver<YearMonth, String>(
        save = { it.toString() },
        restore = { YearMonth.parse(it) }
    )

    var currentMonth by rememberSaveable(stateSaver = yearMonthSaver) {
        mutableStateOf(initYearMonth)
    }

    /**
     * 選取結果, null = 沒選
     */
    var selectedDate by rememberSaveable { mutableStateOf<Int?>(null) }

    // 每次 selectedDate 改變時，對外通知一次
    LaunchedEffect(currentMonth) {
        selectedDate = null
    }

    val days = remember(currentMonth, selectedDate) {
        val selectedSet = selectedDate?.let { setOf(it) } ?: emptySet()
        buildMonthDays(
            yearMonth = currentMonth,
            disableByDate = true,
            selectedDaysOfMonth = selectedSet
        )
    }

    // 選取改變就對外通知，用 currentMonth 組 LocalDate
    LaunchedEffect(selectedDate, currentMonth) {
        val date = selectedDate?.let { currentMonth.atDay(it) } ?: LocalDate.now()
        onSelectionChanged(date)
    }

    Column(
        modifier = modifier
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp) // 可省略
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Previous month"
                )
            }

            val monthTitle = remember(currentMonth) {
                val monthName = currentMonth.month
                    .getDisplayName(TextStyle.FULL, locale)
                "$monthName ${currentMonth.year}"
            }
            Text(
                text = monthTitle,
                style = MaterialTheme.typography.titleMedium
            )

            IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Next month"
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 16.dp),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))

        // 星期標題列
        val weekLabels = twoAlphabetWeekLabels()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            weekLabels.forEach {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // 日期格 7 欄 Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.heightIn(min = 240.dp, max = 360.dp), // 給一個高度，避免無限高度問題
            userScrollEnabled = false                   // 不需要捲動
        ) {
            items(days) { day ->
                DayCell(
                    day = day,
                    onClick = {
                        // 只處理「當月」的格子
                        if (YearMonth.from(day.date) != currentMonth) return@DayCell
                        val date = day.date.dayOfMonth
                        selectedDate =
                            if (selectedDate == date) null // 再點一次同一天就取消選取
                            else date
                    }
                )
            }
        }
    }
}

/**
 * @param onSelectionChanged 回傳的是 Int 形式的 Set
 * ，之後使用需要進行處理日期相關的轉型
 */
@Composable
fun MonthlyCalendar(
    yearMonth: YearMonth,
    selectedDays: Set<Int>,
    onSelectionChanged: (Set<Int>) -> Unit,
    modifier: Modifier = Modifier
) {
    val days = remember(yearMonth, selectedDays) {
        buildMonthDays(
            yearMonth = yearMonth,
            disableByDate = false,
            selectedDaysOfMonth = selectedDays
        )
    }

    Column(
        modifier = modifier
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp) // 可省略
            )
    ) {
        val ruleText = remember(selectedDays) {
            "Every month on ${ selectedDays.sorted().joinToString(", ") }"
        }
        Text(
            text = ruleText,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 16.dp)
        )
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 16.dp),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))

        // 星期標題列
        val weekLabels = twoAlphabetWeekLabels()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            weekLabels.forEach {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // 日期格 7 欄 Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.heightIn(min = 240.dp, max = 360.dp), // 給一個高度，避免無限高度問題
            userScrollEnabled = false                   // 不需要捲動
        ) {
            items(days) { day ->
                DayCell(
                    day = day,
                    onClick = {
                        // 只處理「當月」的格子
                        if (YearMonth.from(day.date) != yearMonth) return@DayCell
                        val d = day.date.dayOfMonth
                        val newSet =
                            if (d in selectedDays)
                                selectedDays - d
                            else
                                selectedDays + d

                        onSelectionChanged(newSet)
                    }
                )
            }
        }
    }
}

@Composable
fun DayCell(
    day: DayUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val textColor = when {
        day.selected -> MaterialTheme.colorScheme.onPrimary
        day.enabled -> MaterialTheme.colorScheme.onSurface
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .clickable(
                    enabled = day.enabled
                ) {
                    onClick()
                }
                .background(
                    color = if (day.selected)
                        MaterialTheme.colorScheme.primary
                    else
                        Color.Transparent
                )
        )

        Text(
            text = day.date.dayOfMonth.toString(),
            color = textColor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (day.selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DateCalendarPreview() {
    var lastChanged by remember { mutableStateOf<LocalDate?>(null) }

    HabitTrackerTheme {
        Column(modifier = Modifier
            .fillMaxWidth()) {
            DateCalendar(
                initYearMonth = YearMonth.now(),
                onSelectionChanged = { newDate ->
                    lastChanged = newDate
                }
            )
            Text(
                text = "目前選擇：$lastChanged",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MonthlyCalendarPreview() {
    var selectedDays by rememberSaveable {
        mutableStateOf<Set<Int>>(emptySet())
    }

    HabitTrackerTheme {
        Column(modifier = Modifier
            .fillMaxWidth()) {
            MonthlyCalendar(
                yearMonth = YearMonth.now(),
                selectedDays = selectedDays,
                onSelectionChanged = { selectedDays = it }
            )
            Text(
                text = "目前選擇：${selectedDays.sorted().joinToString(", ")}",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}