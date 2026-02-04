package com.justplay.habittracker.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItemColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.window.core.layout.WindowSizeClass
import com.justplay.habittracker.data.HomeNavDest
import com.justplay.habittracker.data.MainNavSuiteDest
import com.justplay.habittracker.data.MoodStatDest
import com.justplay.habittracker.data.MyHabitsNavDest
import com.justplay.habittracker.ui.screen.CreateNewHabitScreen
import com.justplay.habittracker.ui.screen.HomeScreen
import com.justplay.habittracker.ui.screen.MoodStatScreen
import com.justplay.habittracker.ui.screen.MyHabitsScreen
import com.justplay.habittracker.ui.screen.ReportScreen
import com.justplay.habittracker.ui.screen.taskDetail.RegularTaskDetailScreen
import com.justplay.habittracker.ui.screen.taskEdit.OneTimeTaskEditScreen
import com.justplay.habittracker.ui.screen.taskEdit.RegularTaskEditScreen

@Composable
fun MainNavSuite() {
    val navHost = rememberNavController()
    val navBackStackEntry by navHost.currentBackStackEntryAsState()

    /**
     * 目前顯示的頁面
     */
    val currDest = navBackStackEntry?.destination

    /**
     * 需要顯示 Navigation 的頁面
     */
    val showSuiteRoutes = MainNavSuiteDest.entries.map { it.name }.toSet()

    /**
     * 判斷是否顯示 NavigationSuite
     */
    val shouldShowSuite = currDest?.route in showSuiteRoutes

    /**
     * 基本上只有手機橫版會達到高度為 COMPACT，因此作為手機橫版的判斷
     *
     * 除非我想做切割視窗否則這夠用了
     */
    val isHeightNotCompact = (currentWindowAdaptiveInfo()
        .windowSizeClass
        .isHeightAtLeastBreakpoint(
            heightDpBreakpoint =
                WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND
        )
    )

    /**
     * NavigationSuite 的顏色設定
     */
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
        navigationSuiteItems =
            getNavigationSuiteItems(currDest, navHost, itemColors),
        modifier = Modifier.fillMaxSize(),
        layoutType = if (!shouldShowSuite) {
            NavigationSuiteType.None
        } else if (!isHeightNotCompact) {
            NavigationSuiteType.NavigationRail
        } else {
            NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(
                currentWindowAdaptiveInfo()
            )
        }
    ) {
        MainNavHost(navHost)
    }
}

@Composable
fun MainNavHost(
    navHost: NavHostController
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
                    MainNavSuiteDest.MOOD_STAT -> MoodStatScreen(onHistoryClick =
                        { navHost.navigate(MoodStatDest.MOOD_STAT_HISTORY.name) }
                    )
                    MainNavSuiteDest.REPORT -> ReportScreen()
                    MainNavSuiteDest.MY_HABITS -> MyHabitsScreen(
                        onEditRegularTask = { taskId ->
                            navHost.navigate("${MyHabitsNavDest.REGULAR_TASK_DETAIL.name}/$taskId")
                        },
                        onEditOneTimeTask = { taskId ->
                            navHost.navigate("${MyHabitsNavDest.EDIT_ONE_TIME_TASK.name}/$taskId")
                        }
                    )
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

        composable(
            route = MyHabitsNavDest.REGULAR_TASK_DETAIL.name + "/{taskId}",
            arguments = listOf(
                navArgument("taskId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments!!.getLong("taskId")

            RegularTaskDetailScreen(
                taskId = taskId,
                onBackClick = { navHost.popBackStack() },
                onEditClick = {
                    navHost.navigate("${MyHabitsNavDest.EDIT_REGULAR_TASK.name}/$taskId")
                }
            )
        }

        composable(
            route = MyHabitsNavDest.EDIT_REGULAR_TASK.name + "/{taskId}",
            arguments = listOf(
                navArgument("taskId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments!!.getLong("taskId")

            RegularTaskEditScreen(
                taskId = taskId,
                onBackClick = { navHost.popBackStack() }
            )
        }

        composable(
            route = MyHabitsNavDest.EDIT_ONE_TIME_TASK.name + "/{taskId}",
            arguments = listOf(
                navArgument("taskId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments!!.getLong("taskId")

            OneTimeTaskEditScreen(
                taskId = taskId,
                onBackClick = { navHost.popBackStack() }
            )
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