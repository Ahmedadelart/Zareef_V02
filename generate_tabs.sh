#!/bin/bash
cat << 'KOTLIN' > app/src/main/java/com/example/ui/shop/tabs/HomeTab.kt
package com.example.ui.shop.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.ui.components.ArtistCircle
import com.example.ui.components.PackRail
import com.example.ui.components.SectionHeader
import com.example.ui.shop.ShopViewModel
import com.example.ui.theme.ChangaFamily
import com.example.ui.theme.Ink
import com.example.ui.theme.Line
import com.example.ui.theme.Muted
import com.example.ui.theme.Surface
import com.example.ui.theme.Accent

@Composable
fun HomeTab(
    viewModel: ShopViewModel,
    onPackClick: (String) -> Unit,
    onArtistClick: (String) -> Unit,
    onSubmitClick: () -> Unit
) {
    val packs by viewModel.packs.collectAsState(initial = emptyList())
    val artists by viewModel.artists.collectAsState(initial = emptyList())
    val recentlyViewedIds by viewModel.recentlyViewed.collectAsState(initial = emptyList())
    
    val editorPick = packs.find { it.isEditorsPick }
    val ownIP = packs.filter { it.isOwnIP }
    val famous = packs.sortedByDescending { it.downloads }.take(10)
    val editorPicks = packs.filter { it.isEditorsPick && it.id != editorPick?.id }
    val newPacks = packs.sortedByDescending { it.publishedAt }.take(10)
    val recentlyViewedPacks = recentlyViewedIds.mapNotNull { id -> packs.find { it.id == id } }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        if (editorPick != null) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .aspectRatio(4f/3f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Surface)
                        .clickable { onPackClick(editorPick.slug) }
                ) {
                    AsyncImage(
                        model = "file:///android_asset/stickers/\${editorPick.trayIconPath}",
                        contentDescription = editorPick.titleAr,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .padding(12.dp)
                            .background(Accent, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.editor_pick),
                            color = Surface,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = ChangaFamily
                        )
                    }
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .background(Color.Black.copy(alpha = 0.4f))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = editorPick.titleAr,
                            color = Surface,
                            fontFamily = ChangaFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = editorPick.artistNameAr,
                            color = Surface.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
        
        item {
            PackRail(
                title = stringResource(R.string.our_characters),
                packs = ownIP,
                onPackClick = onPackClick
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        item {
            PackRail(
                title = stringResource(R.string.famous_stickers),
                packs = famous,
                onPackClick = onPackClick
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        if (artists.isNotEmpty()) {
            item {
                SectionHeader(title = stringResource(R.string.famous_artists))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(artists) { artist ->
                        ArtistCircle(
                            artist = artist,
                            onClick = { onArtistClick(artist.id) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
        
        if (editorPicks.isNotEmpty()) {
            item {
                PackRail(
                    title = stringResource(R.string.our_picks),
                    packs = editorPicks,
                    onPackClick = onPackClick
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
        
        item {
            PackRail(
                title = stringResource(R.string.new_packs),
                packs = newPacks,
                onPackClick = onPackClick
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        if (recentlyViewedPacks.isNotEmpty()) {
            item {
                PackRail(
                    title = stringResource(R.string.view_again),
                    packs = recentlyViewedPacks,
                    onPackClick = onPackClick
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
        
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(Surface, RoundedCornerShape(12.dp))
                    .border(1.dp, Line, RoundedCornerShape(12.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.submit_title),
                    fontFamily = ChangaFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Ink
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.submit_subtitle),
                    fontSize = 14.sp,
                    color = Muted
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = onSubmitClick,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Ink)
                ) {
                    Text(text = stringResource(R.string.submit_action), fontFamily = ChangaFamily, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
KOTLIN

cat << 'KOTLIN' > app/src/main/java/com/example/ui/shop/tabs/RankTab.kt
package com.example.ui.shop.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.model.Pack
import com.example.ui.shop.ShopViewModel
import com.example.ui.theme.Accent
import com.example.ui.theme.ChangaFamily
import com.example.ui.theme.Ink
import com.example.ui.theme.Muted
import com.example.ui.theme.Surface

@Composable
fun RankTab(
    viewModel: ShopViewModel,
    onPackClick: (String) -> Unit
) {
    val packs by viewModel.packs.collectAsState(initial = emptyList())
    val ranked = packs.sortedByDescending { it.downloads }
    
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(ranked) { index, pack ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPackClick(pack.slug) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "\${index + 1}",
                    fontFamily = ChangaFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = if (index < 3) Accent else Muted,
                    modifier = Modifier.width(32.dp)
                )
                
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Surface)
                ) {
                    AsyncImage(
                        model = "file:///android_asset/stickers/\${pack.trayIconPath}",
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = pack.artistNameAr,
                        color = Muted,
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                    Text(
                        text = pack.titleAr,
                        color = Ink,
                        fontFamily = ChangaFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 1
                    )
                }
                
                Text(
                    text = if (pack.isFree) stringResource(R.string.price_free) else stringResource(R.string.price_egp, pack.priceEGP),
                    color = if (pack.isFree) com.example.ui.theme.Free else Ink,
                    fontFamily = ChangaFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
KOTLIN

cat << 'KOTLIN' > app/src/main/java/com/example/ui/shop/tabs/NewTab.kt
package com.example.ui.shop.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import com.example.ui.components.PackCard
import com.example.ui.shop.ShopViewModel

@Composable
fun NewTab(
    viewModel: ShopViewModel,
    onPackClick: (String) -> Unit
) {
    val packs by viewModel.packs.collectAsState(initial = emptyList())
    val newPacks = packs.sortedByDescending { it.publishedAt }
    
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(newPacks) { pack ->
            PackCard(pack = pack, onClick = { onPackClick(pack.slug) })
        }
    }
}
KOTLIN

cat << 'KOTLIN' > app/src/main/java/com/example/ui/shop/tabs/TopArtistsTab.kt
package com.example.ui.shop.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
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
import com.example.ui.shop.ShopViewModel
import com.example.ui.theme.Accent
import com.example.ui.theme.ChangaFamily
import com.example.ui.theme.Ink
import com.example.ui.theme.Muted
import com.example.ui.theme.Surface

@Composable
fun TopArtistsTab(
    viewModel: ShopViewModel,
    onArtistClick: (String) -> Unit
) {
    val artists by viewModel.artists.collectAsState(initial = emptyList())
    
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(artists) { index, artist ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onArtistClick(artist.id) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "\${index + 1}",
                    fontFamily = ChangaFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = if (index < 3) Accent else Muted,
                    modifier = Modifier.width(32.dp)
                )
                
                val color = try { Color(android.graphics.Color.parseColor(artist.accentColorHex)) } catch (e: Exception) { Ink }
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(color),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = artist.avatarInitial,
                        color = Surface,
                        fontFamily = ChangaFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = artist.nameAr,
                        color = Ink,
                        fontFamily = ChangaFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 1
                    )
                    Text(
                        text = "\${artist.totalDownloads} تنزيل",
                        color = Muted,
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
KOTLIN

cat << 'KOTLIN' > app/src/main/java/com/example/ui/shop/tabs/FreeTab.kt
package com.example.ui.shop.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import com.example.ui.components.PackCard
import com.example.ui.shop.ShopViewModel

@Composable
fun FreeTab(
    viewModel: ShopViewModel,
    onPackClick: (String) -> Unit
) {
    val packs by viewModel.packs.collectAsState(initial = emptyList())
    val freePacks = packs.filter { it.isFree }.sortedByDescending { it.publishedAt }
    
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(freePacks) { pack ->
            PackCard(pack = pack, onClick = { onPackClick(pack.slug) })
        }
    }
}
KOTLIN

cat << 'KOTLIN' > app/src/main/java/com/example/ui/shop/tabs/CategoriesTab.kt
package com.example.ui.shop.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.shop.ShopViewModel
import com.example.ui.theme.ChangaFamily
import com.example.ui.theme.Ink
import com.example.ui.theme.Line
import com.example.ui.theme.Muted
import com.example.ui.theme.Surface

@Composable
fun CategoriesTab(
    viewModel: ShopViewModel,
    onCategoryClick: (String) -> Unit
) {
    val categories by viewModel.categories.collectAsState(initial = emptyList())
    val packs by viewModel.packs.collectAsState(initial = emptyList())
    
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories) { category ->
            val previewPack = packs.find { it.id == category.previewPackId }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Surface, RoundedCornerShape(12.dp))
                    .border(1.dp, Line, RoundedCornerShape(12.dp))
                    .clickable { onCategoryClick(category.id) }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (previewPack != null) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(com.example.ui.theme.Paper)
                    ) {
                        AsyncImage(
                            model = "file:///android_asset/stickers/\${previewPack.trayIconPath}",
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = category.nameAr,
                    fontFamily = ChangaFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Ink,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "(\${category.packCount})",
                    fontSize = 14.sp,
                    color = Muted
                )
            }
        }
    }
}
KOTLIN
