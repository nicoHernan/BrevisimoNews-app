package com.example.brevisimo_news.data.remote

import com.example.brevisimo_news.domain.model.BookmarkDto
import com.example.brevisimo_news.domain.model.ProfileDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ProfileApiService {
    @GET("rest/v1/bookmarks")
    suspend fun getUserBookmark(
        @Query("user_id") userId: String,
        @Query("select") select: String = "*"
    ): Response<List<BookmarkDto>>

    @GET ("rest/v1/profiles")
    suspend fun getProfileByFirebaseId(
        @Query("firebase_id") firebaseId: String,
        @Query("select") select: String = "*"
    ): Response<List<ProfileDto>>

    @POST("rest/v1/profiles")
    suspend fun createProfile(
        @Body profile: ProfileDto
    ): Response<Unit>
}