package com.example.brevisimo_news.domain.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookmarkDto(
    @SerializedName("bookmarks_id") val bookmarkId: String? = null,
    @SerializedName("user_id") val userId: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("url") val url: String,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("source_name") val sourceName: String?,
    @SerializedName("created_at") val createdAt: String? = null
)
