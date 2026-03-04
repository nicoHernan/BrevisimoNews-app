package com.example.brevisimo_news.data.repository

import com.example.brevisimo_news.domain.model.ArticleDto
import com.example.brevisimo_news.domain.model.BookmarkDto

interface BookmarkRepository {
    suspend fun saveToBookmarks(bookmark: BookmarkDto) : Result<Unit>
    suspend fun saveArticleAsBookmark(article: ArticleDto, userId: String) : Result<Unit>
    suspend fun getBookmarks(userId: String): Result<List<BookmarkDto>>
    suspend fun deleteBookmark(bookmarkId: String) : Result <Unit>
}