package com.example.brevisimo_news.di


import com.example.brevisimo_news.data.remote.SupabaseApiService
import com.example.brevisimo_news.data.repository.BookmarkImpl
import com.example.brevisimo_news.data.repository.BookmarkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    private const val SUPABASE_KEY =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inp2YWdmaWpkeWZpZGRyenFhYnNsIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjkxODk1OTksImV4cCI6MjA4NDc2NTU5OX0.I_pPfCc1esB5LiFYFzF6DwGaVq73UlbJvhKy3l90xQY"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("apikey", SUPABASE_KEY)
                    .addHeader("Authorization", "Bearer $SUPABASE_KEY")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Prefer", "return=minimal")
                    .build()
                chain.proceed(request)
            }
        .build()
    }

    @Provides
    @Singleton
    @Named("SupabaseRetrofit")
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideSupabaseApiService(@Named("SupabaseRetrofit") retrofit: Retrofit): SupabaseApiService {
        return retrofit.create(SupabaseApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideBookmarkRepository(
        apiService: SupabaseApiService
    ): BookmarkRepository {
        return BookmarkImpl(apiService)
    }
}


