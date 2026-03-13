package com.example.brevisimo_news.screens.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.brevisimo_news.R
import com.example.brevisimo_news.common.GridCardComposable
import com.example.brevisimo_news.common.HorizontalCardComposable
import com.example.brevisimo_news.common.VerticalCardComposable
import com.example.brevisimo_news.domain.model.ArticleDto
import com.example.brevisimo_news.domain.model.MediaDto
import com.example.brevisimo_news.domain.model.SourceDto
import com.example.brevisimo_news.ui.theme.Brevisimo_NewsTheme

@Composable
fun GridArticleItem(
    modifier: Modifier = Modifier,
    articleDto: ArticleDto,
    onClick: () -> Unit,
    onGetEntity: (articleContent: String) -> Unit,
    @DrawableRes previewImage: Int?,
    onSaveClick: () -> Unit,
    isSaved: Boolean = false
) {
    GridCardComposable(
        modifier = modifier,
        articleDto = articleDto,
        onClick = onClick,
        onGetEntity = onGetEntity,
        previewImage = previewImage,
        onSaveClick = onSaveClick,
        isSaved = isSaved
    )
}
@Composable
fun VerticalArticleItem (
    modifier: Modifier = Modifier,
    articleDto: ArticleDto,
    @DrawableRes previewImage: Int?,
    onClick: () -> Unit,
    onGetEntity: (articleContent: String) -> Unit,
    onSaveClick: () -> Unit,
    isSaved: Boolean = false
){
    VerticalCardComposable(
        modifier = modifier,
        articleDto = articleDto,
        previewImage = previewImage,
        onClick = onClick,
        onGetEntity = onGetEntity,
        onSaveClick = onSaveClick,
        isSaved = isSaved
    )
}

@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    name = "VerticalArticleDarkPreview"
)
@Preview
@Composable
fun VerticalArticlePreview() {
    Brevisimo_NewsTheme {
        val articleDto = ArticleDto(
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
        VerticalArticleItem(
            modifier = Modifier,
            articleDto = articleDto,
            previewImage = R.drawable.imagen_para_renderizar,
            onClick = {},
            onGetEntity = {},
            onSaveClick = {}
        )
    }
}
@Composable
fun HorizontalArticleItem (
    modifier: Modifier = Modifier,
    articleDto: ArticleDto,
    @DrawableRes previewImage: Int? = null,
    onClick: () -> Unit,
    onGetEntity: (articleContent: String) -> Unit,
    onSaveClick: () -> Unit,
    isSaved: Boolean = false
){
    HorizontalCardComposable(
        modifier = modifier,
        articleDto = articleDto,
        previewImage = previewImage,
        onClick = onClick,
        onGetEntity = onGetEntity,
        onSaveClick = onSaveClick,
        isSaved = isSaved
    )
}

@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    name = "HorizontalArticleDarkPreview"
)
@Preview(showBackground = true)
@Composable
fun HorizontalArticlePreview() {
    Brevisimo_NewsTheme {
        val articleDto = ArticleDto(
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
        HorizontalArticleItem(
            modifier = Modifier,
            articleDto = articleDto,
            previewImage = R.drawable.imagen_para_renderizar,
            onClick = {},
            onGetEntity = {},
            onSaveClick = {}
        )
    }
}