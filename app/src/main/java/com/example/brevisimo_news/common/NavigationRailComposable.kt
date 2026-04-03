package com.example.brevisimo_news.common

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.StringRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.brevisimo_news.R
import com.example.brevisimo_news.screens.home.NavigationDestination
import com.example.brevisimo_news.ui.theme.Brevisimo_NewsTheme

data class NavigationRailItem(
    val destination: NavigationDestination,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val labelRes: Int,
    val onClickItem: () -> Unit
)
@Composable
fun NavigationRailComposable(
    modifier: Modifier = Modifier,
    @StringRes textHome: Int,
    @StringRes textBookmarks: Int,
    @StringRes textProfile: Int,
    onHomeNavigationIcon: () -> Unit,
    onBookmarksNavigationIcon: () -> Unit,
    onProfileNavigationIcon: () -> Unit,
    onDrawerClick: () -> Unit,
    iconHome: ImageVector,
    iconBookmarks: ImageVector,
    iconProfile: ImageVector,
    iconMenu: ImageVector,
    selectedDestination: NavigationDestination
) {
    val navigationItem = listOf(
        NavigationRailItem(
            destination = NavigationDestination.HOME,
            selectedIcon = iconHome,
            unselectedIcon = Icons.Outlined.Home,
            labelRes = textHome,
            onClickItem = onHomeNavigationIcon
        ),
        NavigationRailItem(
            destination = NavigationDestination.BOOKMARKS,
            selectedIcon = iconBookmarks,
            unselectedIcon = Icons.Outlined.BookmarkBorder,
            labelRes = textBookmarks,
            onClickItem = onBookmarksNavigationIcon
        ),
        NavigationRailItem(
            destination = NavigationDestination.PROFILE,
            selectedIcon = iconProfile,
            unselectedIcon = Icons.Outlined.Person,
            labelRes = textProfile,
            onClickItem = onProfileNavigationIcon
        )
    )
    NavigationRail(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        windowInsets = NavigationBarDefaults.windowInsets,
        header = {
            IconButton(onClick = onDrawerClick) {
                Icon(
                    imageVector = iconMenu,
                    contentDescription = "Abrir menú"
                )
            }
        }
    ) {
        navigationItem.forEach { navigationItem ->
            val isSelected = selectedDestination == navigationItem.destination

            NavigationRailItem(
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
                colors = NavigationRailItemDefaults.colors(
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
    name = "NavigationRailDarkPreview"
)
@Preview(showBackground = true)
@Composable
fun NavigationRailPreview() {
    Brevisimo_NewsTheme{
        NavigationRailComposable(
            modifier = Modifier,
            onDrawerClick = {},
            selectedDestination = NavigationDestination.HOME,
            textProfile = R.string.profile_navigation_bar,
            textHome = R.string.home_navigation_bar ,
            iconMenu = Icons.Default.Menu,
            iconHome = Icons.Default.Home,
            iconProfile = Icons.Filled.Person,
            onHomeNavigationIcon = {},
            onProfileNavigationIcon = {},
            textBookmarks = R.string.bookmarks_navigation_bar,
            iconBookmarks = Icons.Filled.BookmarkBorder,
            onBookmarksNavigationIcon = {}
        )
    }
}