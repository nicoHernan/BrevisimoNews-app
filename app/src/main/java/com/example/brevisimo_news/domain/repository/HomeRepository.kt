package com.example.brevisimo_news.domain.repository

import com.example.brevisimo_news.domain.model.ArticleDto
import com.example.brevisimo_news.domain.model.MediaDto

interface HomeRepository {

    suspend fun getMediaInUs(country: String): List<ArticleDto>

    fun getLocalCategories(): List<String>
    suspend fun getCategoryContent(category: String): List<MediaDto>

    fun getLocalMediaSources(): List<MediaDto>
}