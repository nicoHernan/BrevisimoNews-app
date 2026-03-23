package com.example.brevisimo_news.screens.category

import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.brevisimo_news.NewsAppState
import com.example.brevisimo_news.R
import com.example.brevisimo_news.common.TopNavigationBarComposable
import com.example.brevisimo_news.domain.model.MediaDto
import com.example.brevisimo_news.ui.theme.Brevisimo_NewsTheme

@Composable
fun CategoryScreen (
    modifier: Modifier = Modifier,
    categoryName: String,
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    windowSizeClass: WindowSizeClass,
    newsAppState: NewsAppState
){

    val context = LocalContext.current
    val categoryUiState by categoryViewModel.categoryUiState.collectAsStateWithLifecycle()


    LaunchedEffect(key1 = categoryName){
        categoryViewModel.loadNewsByCategory(category = categoryName)
    }


    LaunchedEffect(Unit) {
        categoryViewModel.categoryEffects.collect { effect ->
            when (effect) {
                is CategorySideEffect.OpenExternalUrl -> {

                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(effect.url))
                    context.startActivity(intent)
                }
            }
        }
    }


    CategoryPortraitLayout(
        modifier = modifier,
        mediaDto = categoryUiState.newsByCategory,
        onSourceSelected = categoryViewModel::onCategoryClick,
        navigateBack = newsAppState::navigateBack
    )
}


@Composable
fun CategoryContent (
    modifier: Modifier = Modifier,
    mediaDto: List<MediaDto>,
    onSourceSelected: (MediaDto) -> Unit
){
    Column(
        modifier = Modifier.fillMaxSize()
    )  {
        if (mediaDto.isEmpty()){

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "No hay noticias para mostrar o la búsqueda no arrojó resultados.",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(all = 32.dp)
                )
            }
        } else{

            LazyColumn (
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(mediaDto) { mediaDto ->
                    CategoryItem(
                        mediaDto = mediaDto,
                        onClick = onSourceSelected
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryPortraitLayout(
    modifier: Modifier = Modifier,
    mediaDto: List<MediaDto>,
    onSourceSelected: (MediaDto) -> Unit,
    navigateBack: () -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopNavigationBarComposable(
                modifier = modifier,
                titleText = R.string.top_bar_category_title,
                onNavigationIconClick = navigateBack,
                navigationIconVector = Icons.Filled.ArrowBack
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues = paddingValues)
                    .fillMaxSize()
            ) {
                CategoryContent(
                    modifier = Modifier.fillMaxSize(),
                    mediaDto = mediaDto,
                    onSourceSelected = onSourceSelected
                )
            }
        },
        bottomBar = {}
    )
}



@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    name = "PortraitDarkPreview"
)
@Preview(showBackground = true)
@Composable
fun PortraitPreview(){

    val mediaDto = List(15){
        MediaDto(
            id = null,
            name = "BBC Sport",
            description = "The home of BBC Sport online. Includes live sports coverage, breaking news, results, video, audio and analysis on Football, F1, Cricket, Rugby Union, Rugby League, Golf, Tennis and all the main world sports, plus major events such as the Olympic Games.",
            url = "http://www.bbc.co.uk/sport",
            category = "sports",
            language = "en",
            country = "gb"
        )
    }

    Brevisimo_NewsTheme {
        CategoryPortraitLayout(
            modifier = Modifier,
            mediaDto = mediaDto,
            onSourceSelected = {},
            navigateBack = {}
        )
    }
}
