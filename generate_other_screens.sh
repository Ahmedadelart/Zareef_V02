#!/bin/bash

cat << 'KOTLIN' > app/src/main/java/com/example/ui/artist/ArtistScreen.kt
package com.example.ui.artist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.PackCard
import com.example.ui.theme.ChangaFamily
import com.example.ui.theme.Ink
import com.example.ui.theme.Muted
import com.example.ui.theme.Paper
import com.example.ui.theme.Surface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistScreen(
    viewModel: ArtistViewModel,
    onBack: () -> Unit,
    onPackClick: (String) -> Unit
) {
    val artist by viewModel.artist.collectAsState()
    val packs by viewModel.packs.collectAsState()
    
    if (artist == null) return
    val a = artist!!
    
    val color = try { Color(android.graphics.Color.parseColor(a.accentColorHex)) } catch (e: Exception) { Ink }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Ink)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Paper)
            )
        },
        containerColor = Paper
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.size(80.dp).clip(CircleShape).background(color),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = a.avatarInitial, color = Surface, fontFamily = ChangaFamily, fontWeight = FontWeight.Bold, fontSize = 32.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = a.nameAr, fontFamily = ChangaFamily, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Ink)
                if (a.bioAr.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = a.bioAr, color = Muted, fontSize = 14.sp)
                }
            }
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(packs) { pack ->
                    PackCard(pack = pack, onClick = { onPackClick(pack.slug) })
                }
            }
        }
    }
}
KOTLIN

cat << 'KOTLIN' > app/src/main/java/com/example/ui/category/CategoryScreen.kt
package com.example.ui.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.PackCard
import com.example.ui.theme.ChangaFamily
import com.example.ui.theme.Ink
import com.example.ui.theme.Paper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    viewModel: CategoryViewModel,
    onBack: () -> Unit,
    onPackClick: (String) -> Unit
) {
    val packs by viewModel.packs.collectAsState()
    val name by viewModel.categoryName.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(name, fontFamily = ChangaFamily, fontWeight = FontWeight.Bold, color = Ink, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Ink)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Paper)
            )
        },
        containerColor = Paper
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.padding(paddingValues)
        ) {
            items(packs) { pack ->
                PackCard(pack = pack, onClick = { onPackClick(pack.slug) })
            }
        }
    }
}
KOTLIN

cat << 'KOTLIN' > app/src/main/java/com/example/ui/submit/SubmitScreen.kt
package com.example.ui.submit

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.Accent
import com.example.ui.theme.ChangaFamily
import com.example.ui.theme.Ink
import com.example.ui.theme.Muted
import com.example.ui.theme.Paper
import com.example.ui.theme.Surface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmitScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Text(
                text = stringResource(R.string.submit_screen_title),
                fontFamily = ChangaFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 32.sp,
                color = Ink
            )
            Spacer(modifier = Modifier.height(32.dp))
            
            Row(modifier = Modifier.fillMaxWidth()) {
                Text("1", fontFamily = ChangaFamily, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Accent, modifier = Modifier.width(32.dp))
                Text(stringResource(R.string.submit_point_1), color = Ink, fontSize = 16.sp, modifier = Modifier.padding(top = 4.dp))
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text("2", fontFamily = ChangaFamily, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Accent, modifier = Modifier.width(32.dp))
                Text(stringResource(R.string.submit_point_2), color = Ink, fontSize = 16.sp, modifier = Modifier.padding(top = 4.dp))
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text("3", fontFamily = ChangaFamily, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Accent, modifier = Modifier.width(32.dp))
                Text(stringResource(R.string.submit_point_3), color = Ink, fontSize = 16.sp, modifier = Modifier.padding(top = 4.dp))
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:hello@zareef.app")
                        putExtra(Intent.EXTRA_SUBJECT, "New Sticker Pack Submission")
                    }
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Accent, contentColor = Surface)
            ) {
                Text(stringResource(R.string.submit_email_button), fontFamily = ChangaFamily, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }
    }
}
KOTLIN
