package com.example.brevisimo_news.screens.profile

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.brevisimo_news.NewsAppState
import com.example.brevisimo_news.R
import com.example.brevisimo_news.common.TopNavigationBarComposable
import com.example.brevisimo_news.ui.theme.Brevisimo_NewsTheme

@Composable
fun ProfileScreen (
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel = hiltViewModel(),
    newsAppState: NewsAppState
){
    val profileUiState by profileViewModel.profileUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        profileViewModel.onProfileImage(uri)
    }

    ProfileContent(
        modifier = modifier,
        profileUiState = profileUiState,
        onPickImage = {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        },
        onLayoutChange = { isGrid ->
            profileViewModel.setLayout(isGrid)
        },
        navigateBack = newsAppState::navigateBack,
        onThemeChange = {isDarkMode ->
            profileViewModel.onThemeChange(isDarkMode)
        },
        onSignOut = {profileViewModel.signOut(context = context)}
    )
}


@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    profileUiState: ProfileUiState,
    onPickImage: () -> Unit,
    onLayoutChange: (Boolean) -> Unit,
    navigateBack: () -> Unit,
    onThemeChange: (Boolean) -> Unit,
    onSignOut: () -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopNavigationBarComposable(
                titleText = R.string.top_bar_profile_title,
                onNavigationIconClick = navigateBack,
                navigationIconVector = Icons.AutoMirrored.Filled.ArrowBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(32.dp))

            Box(contentAlignment = Alignment.BottomEnd) {
                AsyncImage(
                    model = profileUiState.tempLocalUri ?: profileUiState.photoUrl,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { onPickImage() },
                    contentScale = ContentScale.Crop
                )
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(36.dp),
                    shadowElevation = 4.dp
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.padding(8.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = profileUiState.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = profileUiState.email,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(40.dp))

            Text(
                text = "Preferencias de visualización",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            OutlinedCard(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                ListItem(
                    headlineContent = { Text("Diseño de noticias", fontWeight = FontWeight.Medium) },
                    supportingContent = {
                        Text(if (profileUiState.isGridLayout) "Cuadrícula activa" else "Lista clásica")
                    },
                    leadingContent = {
                        Icon(
                            imageVector = if (profileUiState.isGridLayout) Icons.Default.GridView else Icons.Default.ViewList,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    trailingContent = {
                        Switch(
                            checked = profileUiState.isGridLayout,
                            onCheckedChange = { onLayoutChange(it) }
                        )

                    }
                )
            }

            Spacer(Modifier.height(24.dp))

            OutlinedCard(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                ListItem(
                    headlineContent = { Text("Tema de la aplicación", fontWeight = FontWeight.Medium) },
                    supportingContent = {
                        Text(if (profileUiState.isDarkMode) "Modo oscuro" else "Modo claro")
                    },
                    leadingContent = {
                        Icon(
                            imageVector = if (profileUiState.isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    trailingContent = {
                        Switch(
                            checked = profileUiState.isDarkMode,
                            onCheckedChange = { onThemeChange(it) }
                        )
                    }
                )
            }

            Spacer(Modifier.height(80.dp))

            Button(
                onClick = onSignOut,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Logout, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Cerrar Sesión")
            }
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    name = "ProfileDarkPreview"
)

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    Brevisimo_NewsTheme {
        val profileUiState = ProfileUiState(
            isLoading = false,
            name = "Hernán",
            email = "email@gmail.com",
            photoUrl = null,
            isGridLayout = false,
            tempLocalUri = null,
            error = null
        )
        ProfileContent(
            modifier = Modifier,
            profileUiState = profileUiState,
            onPickImage = {},
            onLayoutChange = {},
            navigateBack = {},
            onThemeChange = {},
            onSignOut = {}
        )
    }
}