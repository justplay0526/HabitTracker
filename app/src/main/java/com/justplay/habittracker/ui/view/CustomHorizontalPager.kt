package com.justplay.habittracker.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.justplay.habittracker.R
import com.justplay.habittracker.ui.screen.period.OverallScreen
import com.justplay.habittracker.ui.screen.period.TodayScreen
import com.justplay.habittracker.ui.screen.period.WeeklyScreen
import kotlinx.coroutines.launch

@Composable
fun CustomHorizontalPager(
    tabs: List<String>,
    pages: List<@Composable () -> Unit>,
    modifier: Modifier
) {
    // 警告用
    require(tabs.size == pages.size) {
        "Tabs and Pages should have the same size"
    }

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { tabs.size }
    )

    val scope = rememberCoroutineScope()

    Column(modifier = modifier) {

        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { index, label ->
                val selected = pagerState.currentPage == index

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (selected) MaterialTheme.colorScheme.primary
                            else Color.Transparent
                        )
                        .padding(vertical = 8.dp)
                        .clickable {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = if (selected)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            pages[page].invoke()
        }
    }
}

@PreviewScreenSizes
@Composable
fun CustomHorizontalPagerPreView() {
    val periodTabs = listOf(
        stringResource(R.string.title_period_today),
        stringResource(R.string.title_period_weekly),
        stringResource(R.string.title_period_overall)
    )

    val periodPages: List<@Composable () -> Unit> = listOf(
        { TodayScreen() },
        { WeeklyScreen() },
        { OverallScreen() }
    )

    CustomHorizontalPager(tabs = periodTabs, pages = periodPages, modifier = Modifier.fillMaxSize())
}