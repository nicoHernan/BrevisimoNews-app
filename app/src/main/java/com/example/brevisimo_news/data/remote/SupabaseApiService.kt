package com.example.brevisimo_news.data.remote

import com.example.brevisimo_news.domain.model.BookmarkDto
import com.example.brevisimo_news.domain.model.ProfileDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SupabaseApiService {
    @GET ("rest/v1/profiles")
    suspend fun getProfileByFirebaseId(
        @Query("firebase_id") firebaseId: String,
        @Query("select") select: String = "*"
    ): Response<List<ProfileDto>>

    @POST("rest/v1/profiles")
    suspend fun createProfile(
        @Body profile: ProfileDto
    ): Response<Unit>

    @GET("rest/v1/bookmarks")
    suspend fun getUserBookmark(
        @Query("user_id") userId: String,
        @Query("select") select: String = "*"
    ): Response<List<BookmarkDto>>

    @POST("rest/v1/bookmarks")
    suspend fun addBookmark(
        @Body bookmark: BookmarkDto
    ): Response<Unit>

    @DELETE("rest/v1/bookmarks")
    suspend fun deleteBookmark(
        @Query("bookmarks_id") bookmarksId: String
    ): Response<Unit>
}