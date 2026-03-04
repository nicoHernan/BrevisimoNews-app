package com.example.brevisimo_news.domain.model

import com.google.gson.annotations.SerializedName

data class ProfileDto(
    @SerializedName("firebase_id") val firebaseId: String,
    @SerializedName("email") val email: String?,
    @SerializedName("display_name") val displayName: String?,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("created_at") val createdAt: String? = null
)
