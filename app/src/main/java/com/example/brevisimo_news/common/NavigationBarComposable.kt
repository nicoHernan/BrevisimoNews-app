package com.example.brevisimo_news.common

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.StringRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.brevisimo_news.R
import com.example.brevisimo_news.screens.home.NavigationDestination
import com.example.brevisimo_news.ui.theme.Brevisimo_NewsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigationBarComposable(
    modifier: Modifier = Modifier,
    @StringRes titleText: Int,
    onNavigationIconClick: () -> Unit,
    navigationIconVector: ImageVector
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(titleText).uppercase(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.5.sp,
                    fontSize = 18.sp
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    imageVector = navigationIconVector,
                    contentDescription = "regresar",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.primary
        )
    )
}

data class NavigationItem(
    val destination: NavigationDestination,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val labelRes: Int,
    val onClickItem: () -> Unit
)

@Composable
fun BottomNavigationBarComposable(
    modifier: Modifier = Modifier,
    @StringRes textHome: Int,
    @StringRes textProfile: Int,
    @StringRes textBookmarks: Int,
    iconHome: ImageVector,
    iconBookmarks: ImageVector,
    iconProfile: ImageVector,
    onHomeNavigationIcon: () -> Unit,
    onBookmarksNavigationIcon: () -> Unit,
    onProfileNavigationIcon: () -> Unit,
    selectedDestination: NavigationDestination
) {
    val navigationItem = listOf(
        NavigationItem(
            destination = NavigationDestination.HOME,
            selectedIcon = iconHome,
            unselectedIcon = Icons.Outlined.Home,
            labelRes = textHome,
            onClickItem = onHomeNavigationIcon
        ),
        NavigationItem(
            destination = NavigationDestination.BOOKMARKS,
            selectedIcon = iconBookmarks,
            unselectedIcon = Icons.Outlined.BookmarkBorder,
            labelRes = textBookmarks,
            onClickItem = onBookmarksNavigationIcon
        ),
        NavigationItem(
            destination = NavigationDestination.PROFILE,
            selectedIcon = iconProfile,
            unselectedIcon = Icons.Outlined.Person,
            labelRes = textProfile,
            onClickItem = onProfileNavigationIcon
        )
    )

    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        tonalElevation = 0.dp,
        windowInsets = NavigationBarDefaults.windowInsets
    ) {
        navigationItem.forEach { navigationItem ->
            val isSelected = selectedDestination == navigationItem.destination

            NavigationBarItem(
                selected = isSelected,
                onClick = navigationItem.onClickItem,
                icon = {
                    val iconScale by animateFloatAsState(targetValue = if (isSelected) 1.2f else 1.0f)
                    Icon(
                        imageVector = if (isSelected) navigationItem.selectedIcon else navigationItem.unselectedIcon,
                        contentDescription = stringResource(navigationItem.labelRes),
                        modifier = Modifier.scale(iconScale)
                    )
                },
                label = {
                    Text(
                        text = stringResource(navigationItem.labelRes),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        letterSpacing = if (isSelected) 0.sp else 0.5.sp
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}


@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    name = "TopNavigationDarkPreview"
)
@Preview(showBackground = true)
@Composable
fun TopNavigationPreview(){
    Brevisimo_NewsTheme {
        TopNavigationBarComposable(
           modifier = Modifier,
            titleText = R.string.top_bar_profile_title,
            onNavigationIconClick = {},
            navigationIconVector = Icons.Filled.ArrowBack
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    name = "BottomNavigationDarkPreview"
)
@Preview(showBackground = true)
@Composable
fun BottomNavigationPreview(){
    Brevisimo_NewsTheme {
        BottomNavigationBarComposable(
            modifier = Modifier,
            textHome = R.string.home_navigation_bar,
            textProfile = R.string.profile_navigation_bar,
            iconHome = Icons.Filled.Home,
            iconProfile = Icons.Filled.Person,
            onHomeNavigationIcon = {},
            onProfileNavigationIcon = {},
            selectedDestination = NavigationDestination.HOME,
            textBookmarks = R.string.bookmarks_navigation_bar,
            iconBookmarks = Icons.Filled.BookmarkBorder,
            onBookmarksNavigationIcon = {}
        )
    }
}