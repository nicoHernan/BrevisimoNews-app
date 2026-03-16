package com.example.brevisimo_news.screens.home

import com.example.brevisimo_news.domain.model.ArticleDto
import com.example.brevisimo_news.domain.model.BookmarkDto
import com.example.brevisimo_news.domain.model.MediaDto
import com.example.brevisimo_news.domain.model.SourceDto

data class HomeUiState(
    val userId: String? = null,
    val newsByDomain: List<MediaDto> = emptyList(),
    val categories: List<String> = emptyList(),
    val newsByCategory: List<MediaDto> = emptyList(),
    val newsInUs: List<ArticleDto> = emptyList(),
    val entityName: String = "",
    val entityDescription: String = "",
    val isAiLoading: Boolean = false,
    val isAppLoading: Boolean = false,
    val isError: Boolean = false,
    val valueSearch: String = "",
    val isGuestUser: Boolean = true,
    val isGridLayout: Boolean = false,
    val savedBookmarkUrl: List<String> = emptyList()
)