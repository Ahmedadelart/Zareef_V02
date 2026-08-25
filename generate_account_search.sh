#!/bin/bash

cat << 'KOTLIN' > app/src/main/java/com/example/ui/search/SearchScreen.kt
package com.example.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.PackCard
import com.example.ui.theme.*

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onPackClick: (String) -> Unit
) {
    val query by viewModel.query.collectAsState()
    val results by viewModel.results.collectAsState()
    
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.fillMaxSize().background(Paper)) {
        // Search bar
        Box(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(Surface, RoundedCornerShape(12.dp))
                    .border(if (query.isNotEmpty()) 1.5.dp else 1.dp, if (query.isNotEmpty()) Ink else Line, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Search, contentDescription = null, tint = Ink)
                Spacer(modifier = Modifier.width(8.dp))
                BasicTextField(
                    value = query,
                    onValueChange = { viewModel.onQueryChange(it) },
                    textStyle = TextStyle(color = Ink, fontSize = 16.sp, fontFamily = AlexandriaFamily),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { 
                        viewModel.performSearch()
                        focusManager.clearFocus()
                    }),
                    singleLine = true,
                    cursorBrush = SolidColor(Ink),
                    modifier = Modifier.weight(1f).focusRequester(focusRequester),
                    decorationBox = { innerTextField ->
                        if (query.isEmpty()) {
                            Text("بحث", color = Muted, fontSize = 16.sp, fontFamily = AlexandriaFamily)
                        }
                        innerTextField()
                    }
                )
                if (query.isNotEmpty()) {
                    IconButton(onClick = { viewModel.onQueryChange("") }, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Ink, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }

        if (query.isEmpty()) {
            // Suggestions
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(stringResource(R.string.search_popular), color = Ink, fontFamily = ChangaFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(viewModel.popularSearches) { tag ->
                        Box(
                            modifier = Modifier
                                .border(1.dp, Line, RoundedCornerShape(8.dp))
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { viewModel.setFilter(tag) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(tag, color = Ink, fontSize = 14.sp)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                Text(stringResource(R.string.search_emojis), color = Ink, fontFamily = ChangaFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(viewModel.popularEmojis) { emoji ->
                        Text(emoji, fontSize = 32.sp, modifier = Modifier.clickable { viewModel.setFilter(emoji) })
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                // Filters
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val filters = listOf(R.string.filter_animated, R.string.filter_free, R.string.filter_egyptian, R.string.filter_gulf)
                    items(filters) { f ->
                        val text = stringResource(f)
                        Box(
                            modifier = Modifier
                                .background(Surface, RoundedCornerShape(8.dp))
                                .border(1.dp, Line, RoundedCornerShape(8.dp))
                                .clickable { viewModel.setFilter(text) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(text, color = Ink, fontSize = 14.sp)
                        }
                    }
                }
            }
        } else {
            if (results.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(stringResource(R.string.search_empty), color = Ink, fontFamily = ChangaFamily, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(stringResource(R.string.search_empty_hint), color = Muted, fontSize = 16.sp)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(results) { pack ->
                        PackCard(pack = pack, onClick = { onPackClick(pack.slug) })
                    }
                }
            }
        }
    }
}
KOTLIN

cat << 'KOTLIN' > app/src/main/java/com/example/ui/account/AccountScreen.kt
package com.example.ui.account

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.ui.components.PackCard
import com.example.ui.theme.*

@Composable
fun AccountScreen(
    viewModel: AccountViewModel,
    onSettingsClick: () -> Unit,
    onRedeemClick: () -> Unit,
    onPackClick: (String) -> Unit
) {
    val user by viewModel.currentUser.collectAsState()
    val favouritePacks by viewModel.favouritePacks.collectAsState()
    
    LazyColumn(modifier = Modifier.fillMaxSize().background(Paper)) {
        item {
            if (user == null) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.size(80.dp).background(Line, CircleShape))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(stringResource(R.string.account_welcome), fontFamily = ChangaFamily, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Ink)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(stringResource(R.string.account_signin_prompt), color = Muted, fontSize = 14.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    Spacer(modifier = Modifier.height(24.dp))
                    OutlinedButton(
                        onClick = { viewModel.signIn() },
                        modifier = Modifier.height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Surface, contentColor = Ink)
                    ) {
                        Text(stringResource(R.string.sign_in_google), fontFamily = ChangaFamily, fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.size(80.dp).background(Line, CircleShape).clip(CircleShape)) {
                        AsyncImage(model = user!!.photoUrl, contentDescription = null, modifier = Modifier.fillMaxSize())
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(user!!.displayName, fontFamily = ChangaFamily, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Ink)
                    Text(user!!.email, color = Muted, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        stringResource(R.string.sign_out),
                        color = Muted,
                        fontSize = 14.sp,
                        modifier = Modifier.clickable { viewModel.signOut() }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        if (favouritePacks.isNotEmpty()) {
            item {
                Column(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
                    Text(
                        stringResource(R.string.favorites),
                        fontFamily = ChangaFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Ink,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(favouritePacks) { pack ->
                            PackCard(pack = pack, onClick = { onPackClick(pack.slug) }, modifier = Modifier.width(120.dp))
                        }
                    }
                }
            }
        }
        
        item {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Line, RoundedCornerShape(12.dp))
                        .background(Surface, RoundedCornerShape(12.dp))
                        .clickable { onRedeemClick() }
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.have_gift_code), color = Ink, fontFamily = ChangaFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null, tint = Ink)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Line, RoundedCornerShape(12.dp))
                        .background(Surface, RoundedCornerShape(12.dp))
                        .clickable { onSettingsClick() }
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.settings), color = Ink, fontFamily = ChangaFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null, tint = Ink)
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
KOTLIN

cat << 'KOTLIN' > app/src/main/java/com/example/ui/account/SettingsScreen.kt
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
KOTLIN
