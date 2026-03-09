package com.example.brevisimo_news.screens.profile

import android.content.Context
import android.net.Uri
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brevisimo_news.data.local.LayoutPreferences
import com.example.brevisimo_news.domain.repository.AuthRepository
import com.example.brevisimo_news.domain.Resource
import com.example.brevisimo_news.screens.home.HomeSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val layoutPreferences: LayoutPreferences
): ViewModel(){

    private val _profileUiState = MutableStateFlow(ProfileUiState())
    val profileUiState: StateFlow<ProfileUiState> = _profileUiState.asStateFlow()

    private val _sideEffects = Channel<HomeSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        loadUserData()
        observePreferences()
    }

    fun signOut(context: Context) {
        viewModelScope.launch {
            authRepository.signOut().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val credentialManager = CredentialManager.create(context)
                        credentialManager.clearCredentialState(ClearCredentialStateRequest())

                        _sideEffects.send(HomeSideEffect.NavigateToLogin)
                    }
                    is Resource.Error -> {
                        /* ... */
                    }
                    is Resource.Loading -> { /* ... */ }
                }
            }
        }
    }
    fun onThemeChange(isDark: Boolean) {
        viewModelScope.launch {
            layoutPreferences.setDarkMode(isDark)
        }
    }

    fun onProfileImage(uri: Uri?) {
        _profileUiState.update { currentState ->
            currentState.copy(tempLocalUri = uri)
        }
    }
    private fun observePreferences() {
        viewModelScope.launch {
            combine(
                layoutPreferences.isGridLayout,
                layoutPreferences.isDarkMode
            ) { isGrid, isDark ->
                _profileUiState.update { currentState ->
                    currentState.copy(isGridLayout = isGrid, isDarkMode = isDark)
                }
            }.collect()
        }
    }

    fun setLayout(isGrid: Boolean) {
        viewModelScope.launch {
            layoutPreferences.setLayout(isGrid)
        }
    }

    private fun loadUserData() {
        val user = authRepository.getCurrentUser()
        _profileUiState.update { currentState ->
            currentState.copy(
                name = user?.displayName ?: "Usuario de Brevísimo",
                email = user?.email ?: "Sin correo electrónico",
                photoUrl = user?.photoUrl?.toString()
            )
        }
    }
}