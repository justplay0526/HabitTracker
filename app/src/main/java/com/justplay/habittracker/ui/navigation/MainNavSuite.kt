package com.justplay.habittracker.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.justplay.habittracker.R
import com.justplay.habittracker.ui.screen.HomeScreen
import com.justplay.habittracker.ui.screen.MoodStatScreen
import com.justplay.habittracker.ui.screen.MyHabitsScreen
import com.justplay.habittracker.ui.screen.ReportScreen

enum class MainNavSuiteDest(
    @param:StringRes val title: Int,
    @param:DrawableRes val icon: Int
) {
    HOME(R.string.title_home, R.drawable.round_home_24),
    MOOD_STAT(R.string.title_mood_stat, R.drawable.round_mood_24),
    REPORT(R.string.title_report, R.drawable.round_show_chart_24),
    MY_HABITS(R.string.title_my_habits, R.drawable.round_grid_view_24)
}

@PreviewScreenSizes
@Composable
fun MainNavSuite() {
    /**
     * The current destination of the navigation suite.
     */
    var suiteCurrDest by rememberSaveable { mutableStateOf(MainNavSuiteDest.HOME) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            MainNavSuiteDest.entries.forEach { dest ->
                item(
                    icon = {
                        Icon(
                            painter = painterResource(dest.icon),
                            contentDescription = stringResource(dest.title)
                        )
                    },
                    label = { Text(stringResource(dest.title)) },
                    selected = dest == suiteCurrDest,
                    onClick = { suiteCurrDest = dest }
                )
            }
        }
    ) {
        MainNavSuiteDest.entries.forEach { dest ->
            if (dest == suiteCurrDest) {
                when(dest) {
                    MainNavSuiteDest.HOME -> HomeScreen()
                    MainNavSuiteDest.MOOD_STAT -> MoodStatScreen()
                    MainNavSuiteDest.REPORT -> ReportScreen()
                    MainNavSuiteDest.MY_HABITS -> MyHabitsScreen()
                }
            }
        }
    }
}