package com.example.brevisimo_news

import androidx.compose.material3.DrawerState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.brevisimo_news.screens.home.NavigationDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class NewsAppState (
    val navHostController: NavHostController,
    val windowSizeClass: WindowSizeClass,
    val drawerState: DrawerState,
    private val coroutineScope: CoroutineScope,
    val snackbarHostState: SnackbarHostState
){
    fun showSnackbar(message: String){
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }


    fun navigateBack() {
        navHostController.popBackStack()
    }

    fun navigate(route: String) {
        navHostController.navigate(route) {
            launchSingleTop = true
        }
    }

    fun navigateAndPopUp(route: String, popUp: String) {
        navHostController.navigate(route) {
            launchSingleTop = true
            popUpTo(popUp) {
                inclusive = true
            }
        }
    }

    fun clearAndNavigate(route: String) {
        navHostController.navigate(route) {
            launchSingleTop = true
            popUpTo(0) {
                inclusive = true
            }
        }
    }

    fun openDrawer() {
        coroutineScope.launch {
            drawerState.open()
        }
    }

    fun navigateToCategory(categoryName: String) {
        navHostController.navigate(route = "$CATEGORY_SCREEN/$categoryName") {
            launchSingleTop = true
        }
    }

    fun navigateToDestination(destination: NavigationDestination) {
        val route = when (destination) {
            NavigationDestination.HOME -> HOME_SCREEN
            NavigationDestination.PROFILE -> PROFILE_SCREEN
            NavigationDestination.LOGIN -> LOGIN_SCREEN
            NavigationDestination.BOOKMARKS -> BOOKMARK_SCREEN
        }

        navHostController.navigate(route) {
            popUpTo(navHostController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    @Composable
    fun getCurrentDestination(): NavigationDestination {
        val route = navHostController.currentBackStackEntryAsState().value?.destination?.route

        return when (route) {
            HOME_SCREEN -> NavigationDestination.HOME
            PROFILE_SCREEN -> NavigationDestination.PROFILE
            else -> NavigationDestination.LOGIN
        }
    }
}
