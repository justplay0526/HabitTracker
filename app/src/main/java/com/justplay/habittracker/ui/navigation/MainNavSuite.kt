package com.justplay.habittracker.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItemColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.justplay.habittracker.R
import com.justplay.habittracker.ui.screen.CreateNewHabitScreen
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

enum class HomeNavDest(
) {
    CREATE_NEW_HABIT
}

@PreviewScreenSizes
@Composable
fun MainNavSuite() {
    val navHost = rememberNavController()
    val navBackStackEntry by navHost.currentBackStackEntryAsState()
    val currDest = navBackStackEntry?.destination

    val itemColors = NavigationSuiteDefaults.itemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            indicatorColor = Color.Transparent,
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
    )

    NavigationSuiteScaffold(
        modifier = Modifier.fillMaxSize(),
        navigationSuiteItems =
            getNavigationSuiteItems(currDest, navHost, itemColors)
    ) {
        NavHost(
            navController = navHost,
            startDestination = MainNavSuiteDest.HOME.name
        ) {
            MainNavSuiteDest.entries.forEach { dest ->
                composable(dest.name) {
                    when (dest) {
                        MainNavSuiteDest.HOME -> HomeScreen(onFabClick =
                            { navHost.navigate(HomeNavDest.CREATE_NEW_HABIT.name) }
                        )
                        MainNavSuiteDest.MOOD_STAT -> MoodStatScreen()
                        MainNavSuiteDest.REPORT -> ReportScreen()
                        MainNavSuiteDest.MY_HABITS -> MyHabitsScreen()
                    }
                }
            }
            HomeNavDest.entries.forEach { dest ->
                composable(dest.name) {
                    when (dest) {
                        HomeNavDest.CREATE_NEW_HABIT -> CreateNewHabitScreen(
                            onBackClick = { navHost.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun getNavigationSuiteItems(
    currDest: NavDestination?,
    navHost: NavHostController,
    itemColors: NavigationSuiteItemColors
): NavigationSuiteScope.() -> Unit = {
    MainNavSuiteDest.entries.forEach { dest ->
        /**
         * 是否為被選擇的導航欄
         */
        val isSelected = currDest?.hierarchy?.any {
            it.route == dest.name
        } == true

        item(
            selected = isSelected,
            onClick = {
                navigateWithBackStackHandling(dest.name, navHost)
            },
            label = {
                Text(stringResource(dest.title))
            },
            icon = {
                Icon(painter = painterResource(dest.icon),
                    contentDescription = stringResource(dest.title)
                )
            },
            colors = itemColors
        )
    }
}

private fun navigateWithBackStackHandling(route: String, navHost: NavHostController) {
    navHost.navigate(route) {
        popUpTo(navHost.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}