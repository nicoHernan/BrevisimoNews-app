package com.example.brevisimo_news.data.remote

import com.example.brevisimo_news.domain.model.MediaResponse
import com.example.brevisimo_news.domain.response.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface NewsApiService {

    @GET("v2/top-headlines")
    suspend fun getMediaInUs(
        @Query("apiKey") apiKey: String,
        @Query("country") country: String
    ): NewsResponse


    @GET("v2/top-headlines/sources")
    suspend fun getHeadLinesByCategory(
        @Query("category") category: String,
        @Query("apiKey") apiKey: String
    ): MediaResponse
}