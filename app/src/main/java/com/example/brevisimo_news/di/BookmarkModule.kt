package com.example.brevisimo_news.di


import android.content.Context
import com.example.brevisimo_news.R
import com.example.brevisimo_news.data.remote.BookmarkApiService
import com.example.brevisimo_news.data.repository.bookmark.BookmarkImpl
import com.example.brevisimo_news.domain.repository.BookmarkRepository
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
object BookmarkModule {

    private const val BASE_URL = "https://zvagfijdyfiddrzqabsl.supabase.co/"

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val apiKey = context.getString(R.string.supabase_key)
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("apikey", value = apiKey)
                    .addHeader("Authorization", "Bearer $apiKey")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Prefer", "return=minimal")
                    .build()
                chain.proceed(request)
            }
        .build()
    }

    @Provides
    @Singleton
    @Named("BookmarkRetrofit")
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideBookmarkApiService(@Named("BookmarkRetrofit") retrofit: Retrofit): BookmarkApiService {
        return retrofit.create(BookmarkApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideBookmarkRepository(
        apiService: BookmarkApiService
    ): BookmarkRepository {
        return BookmarkImpl(apiService)
    }
}


