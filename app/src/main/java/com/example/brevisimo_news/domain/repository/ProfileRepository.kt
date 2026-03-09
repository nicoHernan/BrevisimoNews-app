package com.example.brevisimo_news.domain.repository

import com.example.brevisimo_news.domain.model.BookmarkDto

interface ProfileRepository {
    suspend fun getProfileBookmark(userId: String): Result<List<BookmarkDto>>
}