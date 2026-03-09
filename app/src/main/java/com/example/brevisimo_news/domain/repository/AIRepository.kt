package com.example.brevisimo_news.domain.repository

interface AIRepository {
    suspend fun extractKeyEntities(text: String): List<String>
}