package com.example.brevisimo_news.data.repository.profile

import com.example.brevisimo_news.data.remote.ProfileApiService
import com.example.brevisimo_news.domain.repository.ProfileRepository
import com.example.brevisimo_news.domain.model.BookmarkDto
import javax.inject.Inject

class ProfileImpl @Inject constructor(
    private val profileApiService: ProfileApiService
): ProfileRepository {
    override suspend fun getProfileBookmark(userId: String): Result<List<BookmarkDto>> {
            return try {
                val response = profileApiService.getUserBookmark(userId = "eq.$userId")

                if (response.isSuccessful){
                    Result.success(response.body() ?: emptyList())
                } else{
                    Result.failure(Exception("Error en Supabase: ${response.body()}"))
                }
            } catch (e: Exception){
                Result.failure(e)
            }
    }
}