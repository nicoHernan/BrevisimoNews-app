package com.example.brevisimo_news.screens.login

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brevisimo_news.R
import com.example.brevisimo_news.domain.repository.AuthRepository
import com.example.brevisimo_news.domain.Resource
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel(){
    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState = _loginUiState.asStateFlow()


    fun signInAnonymously() {
        viewModelScope.launch {
            _loginUiState.update { it.copy(isLoading = true, isError = null) }


            authRepository.signInAnonymously().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _loginUiState.update { it.copy(
                            isLoading = false,
                            isSuccess = true
                        ) }
                    }
                    is Resource.Error -> {
                        _loginUiState.update { it.copy(
                            isLoading = false,
                            isError = resource.message
                        ) }
                    }
                    is Resource.Loading -> {
                        _loginUiState.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }
    fun signInWithGoogle(context: Context) {
        viewModelScope.launch {
            _loginUiState.update { currenState->
                currenState.copy(isLoading = true)
            }

            val credentialManager = CredentialManager.create(context)

            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(context.getString(R.string.default_web_client_id))
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            try {
                val result = credentialManager.getCredential(context, request)
                handleGoogleResult(result)
            } catch (e: GetCredentialException) {
                _loginUiState.update { currenState->
                    currenState.copy(isLoading = false, isError = e.message)
                }
            }
        }
    }

    private suspend fun handleGoogleResult(result: GetCredentialResponse) {
        val credential = result.credential
        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

            authRepository.signInWithGoogle(googleIdTokenCredential.idToken).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _loginUiState.update { currenState->
                            currenState.copy(isLoading = false, isSuccess = true)
                        }
                    }
                    is Resource.Error -> {
                        _loginUiState.update { currenState->
                            currenState.copy(isLoading = false, isError = resource.message)
                        }
                    }
                    is Resource.Loading -> {
                        _loginUiState.update { currenState->
                            currenState.copy(isLoading = true) }
                    }
                }
            }
        }
    }
}