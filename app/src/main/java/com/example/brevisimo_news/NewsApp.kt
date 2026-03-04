package com.example.brevisimo_news

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.brevisimo_news.common.DrawerComposable
import com.example.brevisimo_news.screens.bookmark.BookmarkScreen
import com.example.brevisimo_news.screens.profile.ProfileScreen
import com.example.brevisimo_news.screens.category.CategoryScreen
import com.example.brevisimo_news.screens.home.HomeScreen
import com.example.brevisimo_news.screens.home.HomeSideEffect
import com.example.brevisimo_news.screens.home.HomeViewModel
import com.example.brevisimo_news.screens.home.NavigationDestination
import com.example.brevisimo_news.screens.login.LoginScreen
import com.example.brevisimo_news.screens.profile.ProfileViewModel
import com.example.brevisimo_news.screens.splash.SplashScreen
import com.example.brevisimo_news.ui.theme.Brevisimo_NewsTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NewsApp(
    windowSizeClass: WindowSizeClass,
    homeViewModel: HomeViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel
) {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        val appState = rememberAppState(windowSizeClass = windowSizeClass, coroutineScope = coroutineScope)

        val profileUiState by profileViewModel.profileUiState.collectAsStateWithLifecycle()
        val uiState by homeViewModel.homeUiState.collectAsStateWithLifecycle()

    Brevisimo_NewsTheme(darkTheme = profileUiState.isDarkMode) {
        LaunchedEffect(Unit) {
            profileViewModel.sideEffects.collect { effect ->
                when (effect) {
                    is HomeSideEffect.NavigateToLogin -> {
                        appState.navigateAndPopUp(LOGIN_SCREEN, PROFILE_SCREEN)
                    }
                    else -> {}
                }
            }
        }

        LaunchedEffect(Unit) {
            homeViewModel.sideEffects.collect { effect ->
                when (effect) {
                    is HomeSideEffect.OpenExternalUrl -> {
                        appState.drawerState.close()

                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(effect.url))
                        context.startActivity(intent)
                    }

                    is HomeSideEffect.NavigateToLogin -> {
                        appState.navigateAndPopUp(LOGIN_SCREEN, HOME_SCREEN)
                    }
                }
            }
        }

        val currentDestination = appState.getCurrentDestination()

        ModalNavigationDrawer(
            drawerState = appState.drawerState,
            gesturesEnabled = currentDestination == NavigationDestination.HOME,
            drawerContent = {
                DrawerComposable(
                    onSourceSelected = { mediaDto ->
                        homeViewModel.onDrawerMediaClick(mediaDto = mediaDto)
                    },
                    text = R.string.drawer_composable,
                    mediaDto = uiState.newsByDomain,
                    onSignOut = {
                        coroutineScope.launch {
                            appState.drawerState.close()
                            homeViewModel.signOut(context)
                        }
                    }
                )
            },
            content = {
                Scaffold() { paddingValues ->
                    NavHost(
                        modifier = Modifier.padding(paddingValues),
                        navController = appState.navHostController,
                        startDestination = SPLASH_SCREEN
                    ) {
                        newsGraph(appState, profileViewModel)
                    }
                }
            }
        )
    }
}


@Composable
fun rememberAppState(
    navHostController: NavHostController = rememberNavController(),
    windowSizeClass: WindowSizeClass,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
): NewsAppState {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    return remember(navHostController, windowSizeClass, coroutineScope, snackbarHostState) {
        NewsAppState(
            navHostController,
            windowSizeClass = windowSizeClass ,
            drawerState = drawerState,
            coroutineScope = coroutineScope,
            snackbarHostState = snackbarHostState
        )
    }
}


fun NavGraphBuilder.newsGraph(newsAppState: NewsAppState, profileViewModel: ProfileViewModel) {
    composable(SPLASH_SCREEN) {
        SplashScreen(
            openAndPopUp = { route, popUp ->
                newsAppState.navigateAndPopUp(route = route, popUp = popUp)
            },
            splashViewModel = hiltViewModel()
        )
    }

    composable(LOGIN_SCREEN) {
        LoginScreen(
            modifier = Modifier,
            onSignInGuest = {
                newsAppState.navigateAndPopUp(HOME_SCREEN, LOGIN_SCREEN)
            },
            onSignInGoogle = {
                newsAppState.navigateAndPopUp(HOME_SCREEN, LOGIN_SCREEN)
            }
        )
    }

    composable(HOME_SCREEN) {
        HomeScreen(
            modifier = Modifier,
            homeViewModel = hiltViewModel(),
            windowSizeClass = newsAppState.windowSizeClass,
            newsAppState = newsAppState
        )
    }

    composable(PROFILE_SCREEN) {
        ProfileScreen(
            modifier = Modifier,
            profileViewModel = profileViewModel,
            newsAppState = newsAppState
        )
    }

    composable(
        route = "$CATEGORY_SCREEN/{category_name}",
        arguments = listOf(
            navArgument(CATEGORY_ARGUMENTS) { type = NavType.StringType }
        )
    ){backstackentry ->
        val categoryName = backstackentry
            .arguments?.getString(CATEGORY_ARGUMENTS) ?: "general"

        CategoryScreen(
            modifier = Modifier,
            categoryViewModel = hiltViewModel(),
            windowSizeClass = newsAppState.windowSizeClass,
            categoryName = categoryName,
            newsAppState = newsAppState
        )
    }
    composable(BOOKMARK_SCREEN) {
        BookmarkScreen(
            modifier = Modifier,
            newsAppState = newsAppState,
            bookmarkViewModel = hiltViewModel()
        )
    }
}