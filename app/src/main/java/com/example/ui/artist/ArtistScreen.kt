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
