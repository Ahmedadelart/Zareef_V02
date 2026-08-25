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
                        model = "file:///android_asset/stickers/${editorPick.trayIconPath}",
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
