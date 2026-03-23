package com.example.brevisimo_news.screens.bookmark

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.brevisimo_news.BOOKMARK_SCREEN
import com.example.brevisimo_news.HOME_SCREEN
import com.example.brevisimo_news.NewsAppState
import com.example.brevisimo_news.R
import com.example.brevisimo_news.common.BookmarkCardComposable
import com.example.brevisimo_news.common.TopNavigationBarComposable
import com.example.brevisimo_news.domain.model.BookmarkDto
import com.example.brevisimo_news.ui.theme.Brevisimo_NewsTheme
import kotlin.Unit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(
    modifier: Modifier = Modifier,
    bookmarkViewModel: BookmarkViewModel = hiltViewModel(),
    newsAppState: NewsAppState
) {
    val bookmarkUiState by bookmarkViewModel.bookmarkUiState.collectAsStateWithLifecycle()
    BookmarkPortraitLayout(
        modifier = modifier,
        bookmarkDto = bookmarkUiState.bookmarkDto,
        navigateBack = {
            newsAppState.navigateBack()
        },
        bookmarkUiState = bookmarkUiState,
        onNavigateToHome = {
            newsAppState.navigateAndPopUp(HOME_SCREEN, BOOKMARK_SCREEN)
        },
        onDelete = bookmarkViewModel::deleteBookmark
    )
}

@Composable
fun BookmarkContent(
    modifier: Modifier = Modifier,
    bookmarkDto: List<BookmarkDto>,
    bookmarkUiState: BookmarkUiState,
    onNavigateToHome: () -> Unit,
    onDelete: (bookmarksId: String) -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        when {
            bookmarkUiState.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            bookmarkDto.isEmpty() -> {
                EmptyBookmarksPlaceholder(
                    onNavigateToHome = onNavigateToHome
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.padding(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items = bookmarkDto) { bookmarkDto ->
                        BookmarkItemWithDismiss(
                            bookmarkDto = bookmarkDto,
                            onDelete = onDelete,
                            onClick = { Toast.makeText(context, "Desliza el Artículo para borrar", Toast.LENGTH_SHORT).show() }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkPortraitLayout(
    modifier: Modifier = Modifier,
    bookmarkDto: List<BookmarkDto>,
    navigateBack: () -> Unit,
    bookmarkUiState: BookmarkUiState,
    onNavigateToHome: () -> Unit,
    onDelete: (bookmarksId: String) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopNavigationBarComposable(
                modifier = modifier,
                titleText = R.string.top_bar_bookmark_title,
                onNavigationIconClick = navigateBack,
                navigationIconVector = Icons.Filled.ArrowBack
            )
        },
        content = { paddingValues ->
            BookmarkContent(
                modifier = Modifier.padding(paddingValues),
                bookmarkDto = bookmarkDto,
                bookmarkUiState = bookmarkUiState,
                onNavigateToHome = onNavigateToHome,
                onDelete = onDelete
            )
        },
            bottomBar = {}
    )
}

@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    name = "BookmarkDarkPreview"
)
@Preview(showBackground = true)
@Composable
fun BookmarkPreview() {
    val bookmarkDto = List(10) {
        BookmarkDto(
            bookmarkId = "",
            userId = "",
            title = "These are the agencies Trump is purging during the shutdown - Axios",
            description = "Thousands are being let go from agencies overseeing health care, the environment and education.",
            url = "https://www.axios.com/2025/10/10/trump-federal-layoffs-treasury-education-health",
            imageUrl = "https://images.axios.com/aQmahivTarO_bl7hCJ7B1Pe5BNQ=/0x73:8076x4616/1366x768/2025/10/10/1760128847374.jpg",
            sourceName = "BBC",
            createdAt = "2025-10-11T15:44:45Z"
        )
    }
    val bookmarkUiState = BookmarkUiState(bookmarkDto = bookmarkDto)
    Brevisimo_NewsTheme {
        BookmarkPortraitLayout(
            modifier = Modifier,
            bookmarkDto = bookmarkDto,
            navigateBack = {},
            bookmarkUiState = bookmarkUiState,
            onNavigateToHome = {},
            onDelete = {}
        )
    }
}

@Composable
fun EmptyBookmarksPlaceholder(
    onNavigateToHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            modifier = Modifier.size(120.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Outlined.BookmarkBorder,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Tu lista está vacía",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Las noticias que guardes en la pantalla de inicio aparecerán aquí para leerlas más tarde.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onNavigateToHome,
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Icon(Icons.Default.Search, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Explorar noticias")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkItemWithDismiss(
    bookmarkDto: BookmarkDto,
    onDelete: (bookmarkId: String) -> Unit,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onDelete(bookmarkDto.bookmarkId ?: "")
                true
            } else false
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val color = if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                MaterialTheme.colorScheme.errorContainer
            } else Color.Transparent

            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        color,
                        RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onErrorContainer)
            }
        },
        enableDismissFromStartToEnd = false
    ) {
        BookmarkCardComposable(
            onClick = {Toast.makeText(context, "Desliza el Artículo para borrar", Toast.LENGTH_SHORT).show()},
            modifier = Modifier,
            bookmark = bookmarkDto
        )
    }
}