package com.example.ui.account

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import com.example.R
import com.example.data.AuthHandler
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    authHandler: AuthHandler
) {
    var showLanguageDialog by remember { mutableStateOf(false) }
    val user by authHandler.currentUser.collectAsState()
    val scope = rememberCoroutineScope()
    
    val currentLanguage = if (AppCompatDelegate.getApplicationLocales().toLanguageTags().contains("en")) {
        stringResource(R.string.language_english)
    } else {
        stringResource(R.string.language_arabic)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings), fontFamily = ChangaFamily, fontWeight = FontWeight.Bold, color = Ink, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Ink)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Paper)
            )
        },
        containerColor = Paper
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            SettingRow(stringResource(R.string.language), value = currentLanguage, onClick = { showLanguageDialog = true })
            SettingRow(stringResource(R.string.privacy_policy))
            SettingRow(stringResource(R.string.terms_of_use))
            SettingRow(stringResource(R.string.about_zareef))
            SettingRow(stringResource(R.string.contact_us))
            
            if (user != null) {
                SettingRow(stringResource(R.string.delete_account), color = Heart, onClick = { 
                    scope.launch { authHandler.deleteAccount() }
                })
            }
            
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.version, "1.0.0"),
                color = Muted,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 24.dp)
            )
        }
    }
    
    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            confirmButton = {},
            containerColor = Surface,
            title = { Text(stringResource(R.string.language), fontFamily = ChangaFamily, fontWeight = FontWeight.Bold, color = Ink) },
            text = {
                Column {
                    Text(
                        stringResource(R.string.language_arabic),
                        color = Ink,
                        modifier = Modifier.fillMaxWidth().clickable {
                            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("ar"))
                            showLanguageDialog = false
                        }.padding(16.dp)
                    )
                    Text(
                        stringResource(R.string.language_english),
                        color = Ink,
                        modifier = Modifier.fillMaxWidth().clickable {
                            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("en"))
                            showLanguageDialog = false
                        }.padding(16.dp)
                    )
                }
            }
        )
    }
}

@Composable
fun SettingRow(title: String, value: String? = null, color: androidx.compose.ui.graphics.Color = Ink, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = color, fontSize = 16.sp)
        if (value != null) {
            Text(value, color = Muted, fontSize = 14.sp)
        }
    }
}
