package com.example.brevisimo_news.domain

sealed class Resource <out T> {
    data object Loading: Resource<Nothing>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val message: String, val exception: Exception? = null) : Resource<Nothing>()
}