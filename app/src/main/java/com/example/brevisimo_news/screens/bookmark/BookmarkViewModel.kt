package com.example.brevisimo_news.screens.bookmark

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brevisimo_news.domain.repository.AuthRepository
import com.example.brevisimo_news.domain.repository.BookmarkRepository
import com.example.brevisimo_news.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val profileRepository: ProfileRepository
): ViewModel() {
    private val _bookmarkUiState = MutableStateFlow(BookmarkUiState())
    val bookmarkUiState: StateFlow<BookmarkUiState> = _bookmarkUiState.asStateFlow()

    init {
        loadBookmark()
    }

    fun loadBookmark() {
        viewModelScope.launch {
            val currentUser = authRepository.getCurrentUser()

            if (currentUser != null && !currentUser.isAnonymous) {

                _bookmarkUiState.update { currentState ->
                    currentState.copy(isAppLoading = true, isError = false)
                }

                val result = profileRepository.getProfileBookmark(currentUser.uid)

                result.onSuccess { bookmarkDto ->
                    _bookmarkUiState.update {currentState->
                        currentState.copy(
                            bookmarkDto = bookmarkDto,
                            isAppLoading = false,
                            isError = false
                        )
                    }
                }.onFailure { error ->
                    Log.e("DATA_ERROR", "Fallo al obtener marcadores: ${error.message}")
                    _bookmarkUiState.update {currentState->
                        currentState.copy(
                            isError = true,
                            isAppLoading = false
                        )
                    }
                }
            } else {
                _bookmarkUiState.update { it.copy(bookmarkDto = emptyList(), isAppLoading = false) }
            }
        }
    }


    fun deleteBookmark(bookmarksId: String) {
        viewModelScope.launch {
            val previousList = _bookmarkUiState.value.bookmarkDto
            _bookmarkUiState.update { currentState ->
                currentState.copy(
                    bookmarkDto = currentState.bookmarkDto.filter { bookmarkDto ->
                        bookmarkDto.bookmarkId != bookmarksId
                    }
                )
            }

            val result = bookmarkRepository.deleteBookmark(bookmarksId)

            result.onFailure { error ->
                Log.e("DELETE_ERROR", "Error al borrar en servidor: ${error.message}")
                _bookmarkUiState.update { bookmarkUiState ->
                    bookmarkUiState.copy(bookmarkDto = previousList)
                }
            }
        }
    }
}