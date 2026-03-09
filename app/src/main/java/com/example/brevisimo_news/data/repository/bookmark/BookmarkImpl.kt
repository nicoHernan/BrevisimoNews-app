package com.example.brevisimo_news.data.repository.bookmark

import com.example.brevisimo_news.data.remote.BookmarkApiService
import com.example.brevisimo_news.domain.repository.BookmarkRepository
import com.example.brevisimo_news.domain.model.ArticleDto
import com.example.brevisimo_news.domain.model.BookmarkDto
import javax.inject.Inject

class BookmarkImpl @Inject constructor(
    private val apiService: BookmarkApiService
) : BookmarkRepository {

    override suspend fun saveToBookmarks(bookmark: BookmarkDto): Result<Unit> {
        return try {
            val response = apiService.addBookmark(bookmark)
            if (response.isSuccessful){
                Result.success(Unit)
            } else{
                Result.failure(Exception("Error al guardar"))
            }
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun saveArticleAsBookmark(
        article: ArticleDto,
        userId: String
    ): Result<Unit> {
        val bookmarkToSave = BookmarkDto(
            userId = userId,
            title = article.title,
            description = article.description,
            url = article.url,
            imageUrl = article.urlToImage,
            sourceName = article.source?.name
        )
        return saveToBookmarks(bookmarkToSave)
    }



    override suspend fun deleteBookmark(bookmarkId: String): Result<Unit> {
        return try {
            val response = apiService.deleteBookmark(bookmarksId = "eq.$bookmarkId")
            if (response.isSuccessful){
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}