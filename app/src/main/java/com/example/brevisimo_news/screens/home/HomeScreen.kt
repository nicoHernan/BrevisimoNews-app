package com.example.brevisimo_news.screens.home


import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.brevisimo_news.NewsAppState
import com.example.brevisimo_news.R
import com.example.brevisimo_news.common.AIDialog
import com.example.brevisimo_news.common.BottomNavigationBarComposable
import com.example.brevisimo_news.common.NavigationRailComposable
import com.example.brevisimo_news.common.SearchComposable
import com.example.brevisimo_news.domain.model.ArticleDto
import com.example.brevisimo_news.domain.model.SourceDto
import com.example.brevisimo_news.ui.theme.Brevisimo_NewsTheme

enum class NavigationDestination {
    HOME,
    PROFILE,
    LOGIN,
    BOOKMARKS
}
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
    windowSizeClass: WindowSizeClass,
    newsAppState: NewsAppState
) {
    val homeUiState by homeViewModel.homeUiState.collectAsStateWithLifecycle()
    val filteredArticles by homeViewModel.filteredArticles.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        homeViewModel.loadSavedUrls()
    }


    when(windowSizeClass.widthSizeClass){
        WindowWidthSizeClass.Compact ->{
            HomePortraitLayout(
                modifier = modifier,
                categories = homeUiState.categories,
                homeUiState = homeUiState,
                onSearch = homeViewModel::onSearch,
                articleDto = filteredArticles,
                openDrawer = newsAppState::openDrawer,
                onCategorySelected = newsAppState::navigateToCategory,
                onArticleDto = homeViewModel::onArticleDto,
                onGetEntity = homeViewModel::getEntity,
                onDismissDialog = homeViewModel::resetAi,
                onHomeIcon = {
                    newsAppState.navigateToDestination(NavigationDestination.HOME)
                },
                onProfileIcon = {
                    if (homeUiState.isGuestUser) {
                        newsAppState.showSnackbar("Debes iniciar sesión con Google")
                    } else {
                        newsAppState.navigateToDestination(NavigationDestination.PROFILE)
                    }
                },
                onBookmarksIcon = {
                    if (homeUiState.isGuestUser) {
                        newsAppState.showSnackbar("Debes iniciar sesión con Google")
                    } else {
                        newsAppState.navigateToDestination(NavigationDestination.BOOKMARKS)
                    }
                },
                snackbarHostState = newsAppState.snackbarHostState,
                selectedDestination = newsAppState.getCurrentDestination(),
                onSaveBookmark = homeViewModel::onSaveBookmark,
                onDeleteBookmark = homeViewModel::deleteBookmark
            )
        }

        WindowWidthSizeClass.Expanded, WindowWidthSizeClass.Medium -> {
            HomeLandscapeLayout(
                modifier = Modifier,
                categories = homeUiState.categories,
                homeUiState = homeUiState,
                onSearch = homeViewModel::onSearch,
                articleDto = filteredArticles,
                openDrawer = newsAppState::openDrawer,
                onCategorySelected = newsAppState::navigateToCategory,
                onArticleDto = homeViewModel::onArticleDto,
                onGetEntity = homeViewModel::getEntity,
                onDismissDialog = homeViewModel::resetAi,
                snackbarHostState = newsAppState.snackbarHostState,
                onHomeClick = {
                    newsAppState.navigateToDestination(NavigationDestination.HOME)
                },
                onProfileClick = {
                    if (homeUiState.isGuestUser) {
                        newsAppState.showSnackbar("Debes iniciar sesión con Google")
                    } else {
                        newsAppState.navigateToDestination(NavigationDestination.PROFILE)
                    }
                },
                selectedDestination = newsAppState.getCurrentDestination(),
                onSaveBookmark = homeViewModel::onSaveBookmark,
                onDeleteBookmark = homeViewModel::deleteBookmark,
                onBookmarksIcon = {
                    if (homeUiState.isGuestUser) {
                        newsAppState.showSnackbar("Debes iniciar sesión con Google")
                    } else {
                        newsAppState.navigateToDestination(NavigationDestination.BOOKMARKS)
                    }
                }
            )
        }
    }
}


@Composable
fun HomeContent (
    modifier: Modifier = Modifier,
    forceHorizontalLayout: Boolean = false,
    categories: List<String>,
    articleDto: List<ArticleDto>,
    homeUiState: HomeUiState,
    onSearch: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onArticleDto: (articleDto: ArticleDto) -> Unit,
    onGetEntity: (articleContent: String) -> Unit,
    onSaveBookmark: (ArticleDto) -> Unit,
    onDeleteBookmark: (articleUrl: String) -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        SearchComposable(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            value = homeUiState.valueSearch,
            onValueChange = onSearch,
            placeholder = R.string.placeholder_search,
            icon = Icons.Filled.Search
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                CategoryFilterItem(
                    modifier = Modifier.fillMaxWidth(),
                    categoryName = category,
                    onClick = onCategorySelected
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        if (articleDto.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay noticias para mostrar o la búsqueda no arrojó resultados.",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(32.dp)
                )
            }
        } else {
            if (forceHorizontalLayout) {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(articleDto) { articleDto ->
                        val isSaved = homeUiState.savedBookmarkUrl.contains(articleDto.url)
                        HorizontalArticleItem(
                            modifier = Modifier.fillMaxWidth(),
                            articleDto = articleDto,
                            previewImage = R.drawable.imagen_para_renderizar,
                            onClick = { onArticleDto(articleDto) },
                            onGetEntity = onGetEntity,
                            onSaveClick = {
                                if (!isSaved) {
                                    onSaveBookmark(articleDto)
                                    Toast.makeText(
                                        context,
                                        "Artículo guardado con éxito",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    onDeleteBookmark(articleDto.url)
                                    Toast.makeText(
                                        context,
                                        "Artículo borrado con éxito",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            isSaved = isSaved
                        )
                    }
                }
            } else {
                if (homeUiState.isGridLayout) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        items(articleDto) { articleDto ->
                            val isSaved = homeUiState.savedBookmarkUrl.contains(articleDto.url)
                            GridArticleItem(
                                modifier = Modifier.fillMaxWidth(),
                                articleDto = articleDto,
                                onClick = { onArticleDto(articleDto) },
                                onGetEntity = onGetEntity,
                                previewImage = R.drawable.imagen_para_renderizar,
                                onSaveClick = {
                                    if (!isSaved) {
                                        onSaveBookmark(articleDto)
                                        Toast.makeText(
                                            context,
                                            "Artículo guardado con éxito",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        onDeleteBookmark(articleDto.url)
                                        Toast.makeText(
                                            context,
                                            "Artículo borrado con éxito",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                },
                                isSaved = isSaved
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        items(articleDto) { articleDto ->
                            val isSaved = homeUiState.savedBookmarkUrl.contains(articleDto.url)
                            VerticalArticleItem(
                                modifier = Modifier.fillMaxWidth(),
                                articleDto = articleDto,
                                previewImage = R.drawable.imagen_para_renderizar,
                                onClick = { onArticleDto(articleDto) },
                                onGetEntity = onGetEntity,
                                onSaveClick = {
                                    if (!isSaved) {
                                        onSaveBookmark(articleDto)
                                        Toast.makeText(
                                            context,
                                            "Artículo guardado con éxito",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        onDeleteBookmark(articleDto.url)
                                        Toast.makeText(
                                            context,
                                            "Artículo borrado con éxito",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                },
                                isSaved = isSaved
                            )
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePortraitLayout (
    modifier: Modifier = Modifier,
    categories: List<String>,
    articleDto: List<ArticleDto>,
    homeUiState: HomeUiState,
    onSearch: (String) -> Unit,
    openDrawer: () -> Unit,
    onCategorySelected: (String) -> Unit,
    onArticleDto: (articleDto: ArticleDto) -> Unit,
    onGetEntity: (articleContent: String) -> Unit,
    onDismissDialog: () -> Unit,
    onHomeIcon: () -> Unit,
    onProfileIcon: () -> Unit,
    onBookmarksIcon: () -> Unit,
    snackbarHostState: SnackbarHostState,
    selectedDestination: NavigationDestination,
    onSaveBookmark: (ArticleDto) -> Unit,
    onDeleteBookmark: (articleUrl: String) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        },
        topBar = {
            CenterAlignedTopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(
                        stringResource(R.string.app_name).uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = openDrawer) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Abrir Menú",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                    modifier = Modifier
                        .padding(paddingValues = paddingValues)
                        .fillMaxSize()
                ) {
                    HomeContent(
                        modifier = Modifier.fillMaxSize(),
                        categories = categories,
                        homeUiState = homeUiState,
                        onSearch = onSearch,
                        articleDto = articleDto,
                        onCategorySelected = onCategorySelected,
                        onArticleDto = onArticleDto,
                        onGetEntity = onGetEntity,
                        onSaveBookmark = onSaveBookmark,
                        onDeleteBookmark = onDeleteBookmark
                    )
                }

                val showAiDialog = homeUiState.isAiLoading || homeUiState.isError || homeUiState.entityName.isNotEmpty()
                if (showAiDialog) {
                    AIDialog(
                        homeUiState = homeUiState,
                        onDismissDialog = onDismissDialog
                    )
                }
        },
        bottomBar = {
            BottomNavigationBarComposable(
                modifier = Modifier.navigationBarsPadding(),
                textHome = R.string.home_navigation_bar,
                textProfile = R.string.profile_navigation_bar,
                iconHome = Icons.Filled.Home,
                iconProfile = Icons.Filled.Person,
                onHomeNavigationIcon = onHomeIcon,
                onProfileNavigationIcon = onProfileIcon,
                selectedDestination = selectedDestination,
                textBookmarks = R.string.bookmarks_navigation_bar,
                iconBookmarks = Icons.Filled.BookmarkBorder,
                onBookmarksNavigationIcon = onBookmarksIcon
            )
        }
    )
}


@Composable
fun HomeLandscapeLayout(
    modifier: Modifier = Modifier,
    categories: List<String>,
    articleDto: List<ArticleDto>,
    homeUiState: HomeUiState,
    onSearch: (String) -> Unit,
    openDrawer: () -> Unit,
    onCategorySelected: (String) -> Unit,
    onArticleDto: (articleDto: ArticleDto) -> Unit,
    onGetEntity: (articleContent: String) -> Unit,
    onDismissDialog: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    selectedDestination: NavigationDestination,
    onSaveBookmark: (ArticleDto) -> Unit,
    onDeleteBookmark: (articleUrl: String) -> Unit,
    onBookmarksIcon: () -> Unit
) {
    Row(modifier = modifier.fillMaxSize()) {
        NavigationRailComposable(
            modifier = Modifier,
            selectedDestination = selectedDestination,
            textHome = R.string.home_navigation_bar,
            textProfile = R.string.profile_navigation_bar,
            onDrawerClick = openDrawer,
            iconHome = Icons.Default.Home,
            iconProfile = Icons.Default.AccountCircle,
            iconMenu = Icons.Default.Menu,
            textBookmarks = R.string.bookmarks_navigation_bar,
            onHomeNavigationIcon = onHomeClick,
            onBookmarksNavigationIcon = onBookmarksIcon,
            onProfileNavigationIcon = onProfileClick,
            iconBookmarks = Icons.Filled.BookmarkBorder
        )

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                HomeContent(
                    modifier = Modifier.fillMaxSize(),
                    forceHorizontalLayout = true,
                    categories = categories,
                    homeUiState = homeUiState,
                    onSearch = onSearch,
                    articleDto = articleDto,
                    onCategorySelected = onCategorySelected,
                    onArticleDto = onArticleDto,
                    onGetEntity = onGetEntity,
                    onSaveBookmark = onSaveBookmark,
                    onDeleteBookmark = onDeleteBookmark
                )
            }
            if (homeUiState.isAiLoading || homeUiState.isError || homeUiState.entityDescription.isNotEmpty()) {
                AIDialog(homeUiState = homeUiState, onDismissDialog = onDismissDialog)
            }
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    name = "PortraitDarkPreview"
)
@Preview(showBackground = true)
@Composable
fun PortraitPreview() {
    val snackbarHostState = remember { SnackbarHostState()}
    val uiState = HomeUiState(valueSearch = "")
    val previewCategories = listOf("Business", "General", "Entertainment", "Health", "Science", "Sports", "Technology")

    val articleDto = List(10){
        ArticleDto(
            source = SourceDto(
                id = "",
                name = "Bbc News"
            ),
            author = "Josephine Walker",
            title = "These are the agencies Trump is purging during the shutdown - Axios",
            description = "Thousands are being let go from agencies overseeing health care, the environment and education.",
            url = "https://www.axios.com/2025/10/10/trump-federal-layoffs-treasury-education-health",
            urlToImage = "https://images.axios.com/aQmahivTarO_bl7hCJ7B1Pe5BNQ=/0x73:8076x4616/1366x768/2025/10/10/1760128847374.jpg",
            publishedAt = "2025-10-11T15:44:45Z",
            content = "What they're saying: A spokesperson for the largest federal employee union, the American Federation of Government Employees, told Axios that the Trump administration is \\\"illegally\\\" firing thousands o… [+4094 chars]"
        )
    }

    Brevisimo_NewsTheme{
        HomePortraitLayout(
            modifier = Modifier,
            categories = previewCategories,
            homeUiState = uiState,
            onSearch = {},
            articleDto = articleDto,
            openDrawer = {},
            onCategorySelected = {},
            onArticleDto = {},
            onGetEntity = {},
            onDismissDialog = {},
            onHomeIcon = {},
            onProfileIcon = {},
            onBookmarksIcon = {},
            snackbarHostState = snackbarHostState,
            selectedDestination = NavigationDestination.HOME,
            onSaveBookmark = {},
            onDeleteBookmark = {}
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    name = "LandscapeDarkPreview"
)
@Preview(showBackground = true)
@Composable
fun LandscapePreview() {
    val snackbarHostState = remember { SnackbarHostState()}
    val uiState = HomeUiState(valueSearch = "")
    val previewCategories = listOf("Business", "General", "Entertainment", "Health", "Science", "Sports", "Technology")

    val articleDto = List(10){
        ArticleDto(
            source = SourceDto(
                id = "",
                name = "Bbc News"
            ),
            author = "Josephine Walker",
            title = "These are the agencies Trump is purging during the shutdown - Axios",
            description = "Thousands are being let go from agencies overseeing health care, the environment and education.",
            url = "https://www.axios.com/2025/10/10/trump-federal-layoffs-treasury-education-health",
            urlToImage = "https://images.axios.com/aQmahivTarO_bl7hCJ7B1Pe5BNQ=/0x73:8076x4616/1366x768/2025/10/10/1760128847374.jpg",
            publishedAt = "2025-10-11T15:44:45Z",
            content = "What they're saying: A spokesperson for the largest federal employee union, the American Federation of Government Employees, told Axios that the Trump administration is \\\"illegally\\\" firing thousands o… [+4094 chars]"
        )
    }

    Brevisimo_NewsTheme{
        HomeLandscapeLayout(
            modifier = Modifier,
            categories = previewCategories,
            homeUiState = uiState,
            onSearch = {},
            articleDto = articleDto,
            openDrawer = {},
            onCategorySelected = {},
            onArticleDto = {},
            onGetEntity = {},
            onDismissDialog = {},
            snackbarHostState = snackbarHostState,
            selectedDestination = NavigationDestination.HOME,
            onHomeClick = {},
            onProfileClick = {},
            onSaveBookmark = {},
            onDeleteBookmark = {},
            onBookmarksIcon = {}
        )
    }
}