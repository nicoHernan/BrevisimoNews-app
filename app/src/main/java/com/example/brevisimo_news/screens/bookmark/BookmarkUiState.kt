package com.example.brevisimo_news.screens.bookmark

import com.example.brevisimo_news.domain.model.BookmarkDto

data class BookmarkUiState(
    val bookmarkDto: List<BookmarkDto> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isAppLoading: Boolean = false
)