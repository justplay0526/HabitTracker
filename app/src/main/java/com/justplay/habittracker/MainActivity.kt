package com.justplay.habittracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.justplay.habittracker.ui.theme.HabitTrackerTheme
import com.justplay.habittracker.ui.view.Greeting

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HabitTrackerTheme {
                HabitTrackerApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun HabitTrackerApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach { dest ->
                item(
                    icon = {
                        Icon(
                            painter = painterResource(dest.icon),
                            contentDescription = stringResource(dest.title)
                        )
                    },
                    label = { Text(stringResource(dest.title)) },
                    selected = dest == currentDestination,
                    onClick = { currentDestination = dest }
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            AppDestinations.entries.forEach { dest ->
                if (dest == currentDestination) {
                    Greeting(
                        name = stringResource(dest.title),
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


enum class AppDestinations(
    @param:StringRes val title: Int,
    @param:DrawableRes val icon: Int
) {
    HOME(R.string.title_home, R.drawable.round_home_24),
    MOOD_STAT(R.string.title_mood_stat, R.drawable.round_mood_24),
    REPORT(R.string.title_report, R.drawable.round_show_chart_24),
    MY_HABITS(R.string.title_my_habits, R.drawable.round_grid_view_24)
}

