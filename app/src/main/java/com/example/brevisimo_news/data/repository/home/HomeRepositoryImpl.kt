package com.example.brevisimo_news.data.repository.home

import com.example.brevisimo_news.data.local.CategoryDataSource
import com.example.brevisimo_news.data.local.MediaDataSource
import com.example.brevisimo_news.data.remote.NewsApiService
import com.example.brevisimo_news.domain.repository.HomeRepository
import com.example.brevisimo_news.domain.model.ArticleDto
import com.example.brevisimo_news.domain.model.MediaDto
import javax.inject.Inject


class HomeRepositoryImpl @Inject constructor(
    private val categoryDataSource: CategoryDataSource,
    private val newsApiService: NewsApiService,
    private val mediaDataSource: MediaDataSource
): HomeRepository {
    override suspend fun getMediaInUs(country: String): List<ArticleDto> {
        return newsApiService.getMediaInUs(country = country).articles
            ?: emptyList()
    }

    override fun getLocalCategories(): List<String> {
        return categoryDataSource.categories.map { sourceDto ->
            sourceDto.name
        }
    }

    override suspend fun getCategoryContent(category: String): List<MediaDto> {
        return newsApiService.getHeadLinesByCategory(category = category).sources
            ?: emptyList()
    }

    override fun getLocalMediaSources(): List<MediaDto> {
       return mediaDataSource.media
    }
}