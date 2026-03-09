package com.example.brevisimo_news.domain.repository

import com.example.brevisimo_news.domain.Resource
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun signInAnonymously(): Flow<Resource<FirebaseUser>>

    fun getCurrentUser(): FirebaseUser?
    fun isUserAnonymous(): Boolean
    suspend fun signInWithGoogle(idToken: String): Flow<Resource<FirebaseUser>>

    suspend fun signOut(): Flow<Resource<Unit>>
}