package com.example.brevisimo_news.common

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.example.brevisimo_news.R
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.brevisimo_news.domain.model.ArticleDto
import com.example.brevisimo_news.domain.model.BookmarkDto
import com.example.brevisimo_news.domain.model.MediaDto
import com.example.brevisimo_news.domain.model.SourceDto
import com.example.brevisimo_news.ui.theme.Brevisimo_NewsTheme


@Composable
fun BookmarkCardComposable(
    modifier: Modifier = Modifier,
    bookmarks: BookmarkDto,
    onClick: () -> Unit
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            if (!bookmarks.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = bookmarks.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .width(120.dp)
                        .fillMaxHeight(),
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = bookmarks.sourceName?.uppercase() ?: "NOTICIA",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = bookmarks.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (!bookmarks.description.isNullOrBlank()) {
                    Text(
                        text = bookmarks.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
@Composable
fun GridCardComposable(
    modifier: Modifier = Modifier,
    articleDto: ArticleDto,
    onClick: () -> Unit,
    onGetEntity: (articleContent: String) -> Unit,
    @DrawableRes previewImage: Int? = null,
    onSaveClick: () -> Unit
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            AsyncImage(
                model = if (LocalInspectionMode.current) {
                    previewImage
                } else{
                    articleDto?.urlToImage
                },
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.imagen_para_renderizar)
            )

            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = articleDto.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = articleDto.source?.name ?: "Fuente desconocida",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1
                    )

                    FilledTonalIconButton(
                        onClick = onSaveClick,
                        shape = RoundedCornerShape(8.dp),
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.BookmarkBorder,
                            contentDescription = "Guardar en marcadores"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { onGetEntity(articleDto.content ?: "") },
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Analizar con IA", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
@Composable
fun CategoryCardComposable(
    modifier: Modifier = Modifier,
    mediaDto: MediaDto,
    onClick: (MediaDto) -> Unit
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        onClick = { onClick(mediaDto) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        MediaSourceIcon(name = mediaDto.name)
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = mediaDto.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Public,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "${mediaDto.country.uppercase()} • ${mediaDto.language.uppercase()}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = mediaDto.description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 20.sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Explorar noticias",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    name = "CategoryCardDarkPreview"
)
@Preview
@Composable
fun CategoryCardPreview() {
    Brevisimo_NewsTheme {
        val mediaDto = MediaDto(
            id = null,
            name = "BBC Sport",
            description = "The home of BBC Sport online. Includes live sports coverage, breaking news, results, video, audio and analysis on Football, F1, Cricket, Rugby Union, Rugby League, Golf, Tennis and all the main world sports, plus major events such as the Olympic Games.",
            url = "http://www.bbc.co.uk/sport",
            category = "sports",
            language = "en",
            country = "gb"
        )
        CategoryCardComposable(
            modifier = Modifier,
            onClick = {},
            mediaDto = mediaDto
        )
    }
}


@Composable
fun VerticalCardComposable (
    modifier: Modifier = Modifier,
    articleDto: ArticleDto? = null,
    @DrawableRes previewImage: Int? = R.drawable.imagen_para_renderizar,
    onClick: () -> Unit,
    onGetEntity: (articleContent: String) -> Unit,
    onSaveClick: () -> Unit
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column() {
            AsyncImage(
                    model = if (LocalInspectionMode.current) {
                        previewImage
                    } else{
                        articleDto?.urlToImage
                    },
                placeholder = if (previewImage != null) painterResource(id = previewImage) else null,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = articleDto?.source?.name?.uppercase() ?: "UNKNOWN SOURCE",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = articleDto?.publishedAt?.take(10) ?: "", // Solo la fecha YYYY-MM-DD
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = articleDto?.title ?: "Título no disponible",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(16.dp))

                val articleContent = articleDto?.content
                val isContentAvailable = !articleContent.isNullOrBlank()

                OutlinedButton(
                    onClick = { if (isContentAvailable) onGetEntity(articleContent!!) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isContentAvailable,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isContentAvailable) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outline
                    ),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = if (isContentAvailable) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Analizar con IA",
                        style = MaterialTheme.typography.labelLarge,
                        color = if (isContentAvailable) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    )
                }
                FilledTonalIconButton(
                    onClick = onSaveClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder,
                        contentDescription = "Guardar en marcadores"
                    )
                }
            }
        }
    }
}

@Composable
fun HorizontalCardComposable(
    modifier: Modifier = Modifier,
    articleDto: ArticleDto,
    @DrawableRes previewImage: Int? = null,
    onClick: () -> Unit,
    onGetEntity: (articleContent: String) -> Unit,
    onSaveClick: () -> Unit
) {
    val isContentAvailable = !articleDto.content.isNullOrBlank()

    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = if (LocalInspectionMode.current) {
                    previewImage
                } else {
                    articleDto.urlToImage
                },
                placeholder = if (previewImage != null) painterResource(id = previewImage) else null,
                contentDescription = null,
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = articleDto.source?.name?.uppercase() ?: "UNKNOWN",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = articleDto.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = articleDto.publishedAt.take(10),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Surface(
                        onClick = { if (isContentAvailable) onGetEntity(articleDto.content!!) },
                        enabled = isContentAvailable,
                        shape = RoundedCornerShape(20.dp),
                        color = if (isContentAvailable)
                            MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        contentColor = if (isContentAvailable)
                            MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text("Analizar con IA", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                    FilledTonalIconButton(
                        onClick = onSaveClick,
                        shape = RoundedCornerShape(8.dp),
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.BookmarkBorder,
                            contentDescription = "Guardar en marcadores"
                        )
                    }
                }
            }
        }
    }
}



@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    name = "GridDarkPreview"
)
@Preview
@Composable
fun GridPreview() {
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
        GridCardComposable(
            modifier = Modifier,
            articleDto = articleDto,
            onClick = {},
            onGetEntity = {},
            previewImage = R.drawable.imagen_para_renderizar,
            onSaveClick = {}
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    name = "HorizontalDarkPreview"
)
@Preview
@Composable
fun HorizontalPreview() {
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
        HorizontalCardComposable(
            modifier = Modifier,
            articleDto = articleDto,
            previewImage = R.drawable.imagen_para_renderizar ,
            onClick = {},
            onGetEntity = {},
            onSaveClick = {}
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    name = "VerticalDarkPreview"
)
@Preview(showBackground = true)
@Composable
fun VerticalPreview() {
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
        VerticalCardComposable(
            modifier = Modifier,
            articleDto = articleDto,
            previewImage = R.drawable.imagen_para_renderizar ,
            onClick = {},
            onGetEntity = {},
            onSaveClick = {}
        )
    }
}