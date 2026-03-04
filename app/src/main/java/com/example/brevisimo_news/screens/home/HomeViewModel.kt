package com.example.brevisimo_news.screens.home

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brevisimo_news.data.local.LayoutPreferences
import com.example.brevisimo_news.data.repository.AIRepository
import com.example.brevisimo_news.data.repository.AuthRepository
import com.example.brevisimo_news.data.repository.BookmarkRepository
import com.example.brevisimo_news.data.repository.HomeRepository
import com.example.brevisimo_news.data.repository.Resource
import com.example.brevisimo_news.domain.model.ArticleDto
import com.example.brevisimo_news.domain.model.BookmarkDto
import com.example.brevisimo_news.domain.model.MediaDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
    private val homeRepository: HomeRepository,
    private val aiRepository: AIRepository,
    private val authRepository: AuthRepository,
    private val layoutPreferences: LayoutPreferences
    ) : ViewModel() {
    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    private val _sideEffects = Channel<HomeSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        checkUserStatus()
        observeLayoutPreference()
        loadListOfCategory()
        loadNewsInUs()
        loadMediaSourcesForDrawer()
    }

    fun onSaveBookmark(article: ArticleDto) {
        viewModelScope.launch {
            val userId = _homeUiState.value.userId

            if (userId != null) {
                val result = bookmarkRepository.saveArticleAsBookmark(article, userId)

                result.onSuccess {
                    Log.d("SUPABASE", "Noticia guardada correctamente")
                }.onFailure { error ->
                    Log.e("SUPABASE", "Error al guardar: ${error.message}")
                }
            } else {
                Log.w("SUPABASE", "Intento de guardar sin estar logueado")
            }
        }
    }


    private fun checkUserStatus() {
        val currentUser = authRepository.getCurrentUser()
        val isAnonymous = authRepository.isUserAnonymous()

        _homeUiState.update { currentState ->
            currentState.copy(
                isGuestUser = isAnonymous,
                userId = if (!isAnonymous){
                    currentUser?.uid
                } else{
                    null
                }
            )
        }
    }
    private fun observeLayoutPreference() {
        viewModelScope.launch {
            layoutPreferences.isGridLayout.collect { isGrid ->
                _homeUiState.update { currentState ->
                    currentState.copy(isGridLayout = isGrid)
                }
            }
        }
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


     fun getEntity(articleContent: String) {
        viewModelScope.launch {
            _homeUiState.update { currentState ->
                currentState.copy(isAiLoading = true, isError = false)
            }

            try {
                val result = aiRepository.extractKeyEntities(text = articleContent )
                _homeUiState.update { currentState->
                    currentState.copy(
                        entityName = result[0],
                        entityDescription = result[1],
                        isAiLoading = false
                    )
                }
            } catch (e: Exception) {
                Log.e("DATA_ERROR", "Fallo al obtener noticias: ${e.message}", e)
                _homeUiState.update { currentState ->
                    currentState.copy(
                        isError = true,
                        isAiLoading = false,
                        newsInUs = emptyList()
                    )
                }
            }
        }
    }

    fun resetAi() {
        _homeUiState.update {currentState ->
            currentState.copy(
                isAiLoading = false,
                isError = false,
                entityName = "",
                entityDescription = ""
            )
        }
    }

    private fun loadMediaSourcesForDrawer() {
        val mediaSources = homeRepository.getLocalMediaSources()

        _homeUiState.update { currentState ->
            currentState.copy(
                newsByDomain = mediaSources
            )
        }
    }

    fun onDrawerMediaClick(mediaDto: MediaDto) {
        viewModelScope.launch {
            mediaDto.url.let { url ->
                if (url.isNotEmpty()) {
                    _sideEffects.send(HomeSideEffect.OpenExternalUrl(url))
                }
            }
        }
    }

    fun onArticleDto(articleDto: ArticleDto) {
        viewModelScope.launch {
            articleDto.url.let { url ->
                if (url.isNotEmpty()) {
                    _sideEffects.send(HomeSideEffect.OpenExternalUrl(url))
                }
            }
        }
    }

    private fun loadNewsInUs() {
        viewModelScope.launch {
            _homeUiState.update { currentState ->
                currentState.copy(isAppLoading = true, isError = false)
            }

            try {
                val newsInUs = homeRepository.getMediaInUs(country = "us" )
                _homeUiState.update { currentState->
                    currentState.copy(
                        newsInUs = newsInUs,
                        isAppLoading = false
                    )
                }
            } catch (e: Exception) {
                Log.e("DATA_ERROR", "Fallo al obtener noticias: ${e.message}", e)
                _homeUiState.update { currentState ->
                    currentState.copy(
                        isError = true,
                        isAppLoading = false,
                        newsInUs = emptyList()
                    )
                }
            }
        }
    }


    private fun loadListOfCategory() {
        _homeUiState.update { currentState ->
            currentState.copy(categories = homeRepository.getLocalCategories())
        }
    }

    val filteredArticles: StateFlow<List<ArticleDto>> = homeUiState
            .map { uiState ->
                val originalList = uiState.newsInUs
                val query = uiState.valueSearch.trim().lowercase()

                if (query.isBlank()) {
                    originalList
                } else {

                    originalList.filter { article ->

                        val titleMatch = article.title.lowercase().contains(query)
                        val descriptionMatch = article.description?.lowercase()?.contains(query) ?: false

                        titleMatch || descriptionMatch
                    }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    fun onSearch(newValue: String){
        _homeUiState.update { currentState->
            currentState.copy(valueSearch = newValue)
        }
    }
}