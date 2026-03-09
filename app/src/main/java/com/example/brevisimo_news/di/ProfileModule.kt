package com.example.brevisimo_news.di

import com.example.brevisimo_news.data.remote.ProfileApiService
import com.example.brevisimo_news.data.repository.profile.ProfileImpl
import com.example.brevisimo_news.domain.repository.ProfileRepository
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
object ProfileModule {
    private const val BASE_URL = "https://zvagfijdyfiddrzqabsl.supabase.co/"

    @Provides
    @Singleton
    @Named("ProfileRetrofit")
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ProfileModule.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideProfileApiService(
        @Named("ProfileRetrofit") retrofit: Retrofit
    ): ProfileApiService {
        return retrofit.create(ProfileApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(
        apiService: ProfileApiService
    ): ProfileRepository {
        return ProfileImpl(apiService)
    }
}