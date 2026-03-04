package com.example.brevisimo_news

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.brevisimo_news.screens.profile.ProfileViewModel
import com.example.brevisimo_news.ui.theme.Brevisimo_NewsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val profileViewModel: ProfileViewModel = hiltViewModel()
            val profileUiState by profileViewModel.profileUiState.collectAsStateWithLifecycle()

            Brevisimo_NewsTheme(darkTheme = profileUiState.isDarkMode) {
                val windowSizeClass = calculateWindowSizeClass(this)
                NewsApp(windowSizeClass, profileViewModel = profileViewModel)
            }
        }
    }
}