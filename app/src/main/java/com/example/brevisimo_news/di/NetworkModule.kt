package com.example.brevisimo_news.di

import com.example.brevisimo_news.data.remote.NewsApiService
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://newsapi.org/"

    @Provides
    @Singleton
    @Named("NewsRetrofit")
    fun provideRetrofit(): Retrofit{
        val gson = GsonBuilder()
            .create()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsApiService(@Named("NewsRetrofit") retrofit: Retrofit): NewsApiService {
        return retrofit.create(NewsApiService::class.java)
    }
}