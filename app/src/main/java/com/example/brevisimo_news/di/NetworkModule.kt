package com.example.brevisimo_news.di

import android.content.Context
import com.example.brevisimo_news.R
import com.example.brevisimo_news.data.remote.NewsApiService
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
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
    @Named("NewsOkHttpClient")
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val apiKey = context.getString(R.string.news_key)
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val originalHttpUrl = original.url

                val url = originalHttpUrl
                    .newBuilder()
                    .addQueryParameter("apiKey", value = apiKey)
                    .build()

                val requestBuilder = original
                    .newBuilder()
                    .url(url)

                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .build()
    }

    @Provides
    @Singleton
    @Named("NewsRetrofit")
    fun provideRetrofit(@Named("NewsOkHttpClient") okHttpClient: OkHttpClient): Retrofit{
        val gson = GsonBuilder()
            .create()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsApiService(@Named("NewsRetrofit") retrofit: Retrofit): NewsApiService {
        return retrofit.create(NewsApiService::class.java)
    }
}