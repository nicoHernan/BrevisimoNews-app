package com.example.brevisimo_news.screens.login

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.GMobiledata
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.brevisimo_news.R
import com.example.brevisimo_news.common.ButtonComposable
import com.example.brevisimo_news.common.OutlinedButtonComposable
import com.example.brevisimo_news.ui.theme.Brevisimo_NewsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = hiltViewModel(),
    windowSizeClass: WindowSizeClass,
    onSignInGuest: () -> Unit,
    onSignInGoogle: () -> Unit
) {
    val context = LocalContext.current
    val loginUiState by loginViewModel.loginUiState.collectAsStateWithLifecycle()

    LaunchedEffect(loginUiState.isSuccess) {
        if (loginUiState.isSuccess) {
            onSignInGoogle()
        }
    }

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            LoginPortraitLayout(
                modifier = modifier,
                loginUiState = loginUiState,
                onAnonymously = loginViewModel::signInAnonymously,
                onGoogle = loginViewModel::signInWithGoogle
            )
        }
    }
}

@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    loginUiState: LoginUiState,
    onAnonymously: () -> Unit,
    onGoogle: (context: Context) -> Unit
) {
    val context = LocalContext.current
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ButtonComposable(
                modifier = Modifier.fillMaxWidth(),
                onClick = onAnonymously,
                text = R.string.button_composable,
                icon = Icons.Filled.FlashOn
            )
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
                Text(
                    " O ",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
            }
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButtonComposable(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onGoogle(context) },
                text = R.string.outlinedButton_composable,
                icon = Icons.Filled.GMobiledata
            )

            loginUiState.isError?.let { error ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }
        }
        if (loginUiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPortraitLayout(
    modifier: Modifier = Modifier,
    loginUiState: LoginUiState,
    onAnonymously: () -> Unit,
    onGoogle: (context: Context) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceContainerLow
            ) {
                Text(
                    text = stringResource(R.string.bottom_bar_text),
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    ){ paddingValues ->
        Column (
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(64.dp))
            AppLogoAndName(appName = stringResource(R.string.app_name))
            LoginContent(
                modifier = Modifier.weight(1f),
                loginUiState = loginUiState,
                onAnonymously = onAnonymously,
                onGoogle = onGoogle
            )
        }
    }
}

@Composable
fun AppLogoAndName(
    appName: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.FlashOn,
            contentDescription = "Logo de la aplicación",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(72.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = appName,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = stringResource(R.string.app_description),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PortraitPreview() {
    Brevisimo_NewsTheme {
        val loginUiState = LoginUiState(isLoading = true)
        LoginPortraitLayout(
            modifier = Modifier,
            loginUiState = loginUiState,
            onAnonymously = {},
            onGoogle = {}
        )
    }
}