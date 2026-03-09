package com.example.brevisimo_news.data.remote

import com.example.brevisimo_news.domain.model.BookmarkDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BookmarkApiService {
    @POST("rest/v1/bookmarks")
    suspend fun addBookmark(
        @Body bookmark: BookmarkDto
    ): Response<Unit>

    @DELETE("rest/v1/bookmarks")
    suspend fun deleteBookmark(
        @Query("bookmarks_id") bookmarksId: String
    ): Response<Unit>
}