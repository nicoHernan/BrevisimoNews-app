package com.example.brevisimo_news.data.repository.auth

import com.example.brevisimo_news.data.remote.ProfileApiService
import com.example.brevisimo_news.domain.Resource
import com.example.brevisimo_news.domain.model.ProfileDto
import com.example.brevisimo_news.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val profileApiService: ProfileApiService
) : AuthRepository {

    override suspend fun signInAnonymously(): Flow<Resource<FirebaseUser>> = flow {
        emit(Resource.Loading)
        try {
            val authResult = firebaseAuth.signInAnonymously().await()
            val user = authResult.user

            if (user != null) {
                val newProfile = ProfileDto(
                    firebaseId = user.uid,
                    email = null,
                    displayName = "Invitado",
                    avatarUrl = null
                )
                val profileResponse = profileApiService.createProfile(newProfile)
                if (profileResponse.isSuccessful) {
                    emit(Resource.Success(user))
                } else {
                    emit(Resource.Error("Firebase OK, pero fallo al crear perfil en Supabase: ${profileResponse.code()}"))
                }
            } else {
                emit(Resource.Error("El usuario de Firebase es nulo después de la autenticación anónima."))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Fallo en la autenticación anónima: ${e.localizedMessage}", e))
        }
    }

    override fun isUserAnonymous(): Boolean {
        return firebaseAuth.currentUser?.isAnonymous ?: true
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override suspend fun signInWithGoogle(idToken: String): Flow<Resource<FirebaseUser>> = flow {
        emit(Resource.Loading)
        try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            val user = authResult.user

            if (user != null) {
                val profileResponse =
                    profileApiService.getProfileByFirebaseId(firebaseId = "eq.${user.uid}")

                if (profileResponse.isSuccessful) {
                    val existingProfiles = profileResponse.body()

                    if (!existingProfiles.isNullOrEmpty()) {
                        emit(Resource.Success(user))
                        return@flow
                    }

                    val newProfile = ProfileDto(
                        firebaseId = user.uid,
                        email = user.email,
                        displayName = user.displayName,
                        avatarUrl = user.photoUrl?.toString()
                    )
                    val createResponse = profileApiService.createProfile(newProfile)

                    if (createResponse.isSuccessful) {
                        emit(Resource.Success(user))
                    } else {
                        emit(Resource.Error("Error al registrar perfil en la base de datos"))
                    }
                } else {
                    emit(Resource.Error("Error de conexión con el servidor de perfiles"))
                }
            } else {
                emit(Resource.Error("Error al obtener usuario de Google"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Error desconocido"))
        }
    }

    override suspend fun signOut(): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            firebaseAuth.signOut()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error("Error al cerrar sesión: ${e.localizedMessage}"))
        }
    }
}