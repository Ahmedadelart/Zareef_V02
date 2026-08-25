package com.example.ui.pack

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.model.Sticker
import com.example.ui.components.PackRail
import com.example.ui.components.StickerTile
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PackScreen(
    viewModel: PackViewModel,
    onBack: () -> Unit,
    onArtistClick: (String) -> Unit,
    onCategoryClick: (String) -> Unit,
    onPackClick: (String) -> Unit
) {
    val pack by viewModel.pack.collectAsState()
    val artist by viewModel.artist.collectAsState()
    val stickers by viewModel.stickers.collectAsState()
    val similarPacks by viewModel.similarPacks.collectAsState()
    val moreByArtist by viewModel.moreByArtist.collectAsState()
    
    val favouritesList by viewModel.favourites.collectAsState(initial = emptyList())
    val recentlyViewedIds by viewModel.recentlyViewed.collectAsState(initial = emptyList())
    
    val context = LocalContext.current
    var selectedSticker by remember { mutableStateOf<Sticker?>(null) }
    var descriptionExpanded by remember { mutableStateOf(false) }

    if (pack == null) return

    val p = pack!!
    val isFavourite = favouritesList.contains(p.id)
    val recentlyViewedPacks = recentlyViewedIds.mapNotNull { id -> 
        if (id == p.id) null else null // Simplified for mock, ideally fetch from VM
    } // Need to inject all packs here, but we can rely on similarPacks for now. Actually viewmodel provides recentlyViewedIds.
    // For simplicity I will just skip recentlyViewed in PackScreen or implement it properly in ViewModel.

    val artistColor = try { Color(android.graphics.Color.parseColor(artist?.accentColorHex ?: "#000000")) } catch(e: Exception) { Ink }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Ink)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "https://zareef.app/p/${p.slug}")
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, null))
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = Ink)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Paper)
            )
        },
        containerColor = Paper
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Hero
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .background(artistColor.copy(alpha = 0.08f)),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = "file:///android_asset/stickers/${p.trayIconPath}",
                        contentDescription = p.titleAr,
                        modifier = Modifier
                            .fillMaxSize(0.8f)
                            .padding(24.dp),
                        contentScale = ContentScale.Fit
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Artist Chip
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Row(
                        modifier = Modifier
                            .border(1.dp, Line, CircleShape)
                            .clip(CircleShape)
                            .clickable { onArtistClick(p.artistId) }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = p.artistNameAr, color = Ink, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = ChangaFamily)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Title
            item {
                Text(
                    text = p.titleAr,
                    fontFamily = ChangaFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 32.sp,
                    color = Ink,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = if (p.isFree) stringResource(R.string.price_free) else stringResource(R.string.price_egp, p.priceEGP),
                    color = Muted,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            // Action Row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .border(1.dp, Line, RoundedCornerShape(12.dp))
                            .clickable { viewModel.toggleFavourite() },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (isFavourite) Heart else Muted,
                                modifier = Modifier.size(20.dp)
                            )
                            Text("${p.favourites + if(isFavourite) 1 else 0}", fontSize = 10.sp, color = Muted)
                        }
                    }
                    
                    if (p.isFree) {
                        OutlinedButton(
                            onClick = { viewModel.giftPack() },
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Ink)
                        ) {
                            Text(stringResource(R.string.share_free), fontFamily = ChangaFamily, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = { viewModel.addToWhatsApp(context) },
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Accent, contentColor = Surface)
                        ) {
                            Text(stringResource(R.string.add_to_whatsapp), fontFamily = ChangaFamily, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        OutlinedButton(
                            onClick = { viewModel.giftPack() },
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Ink)
                        ) {
                            Text(stringResource(R.string.gift_it), fontFamily = ChangaFamily, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = { viewModel.buyPack() },
                            modifier = Modifier.weight(1.5f).height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Accent, contentColor = Surface)
                        ) {
                            Text(stringResource(R.string.buy), fontFamily = ChangaFamily, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            // Description
            if (p.descriptionAr.isNotBlank()) {
                item {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .clickable { descriptionExpanded = !descriptionExpanded }
                        .padding(horizontal = 16.dp)) {
                        Text(
                            text = p.descriptionAr,
                            color = Ink,
                            fontSize = 14.sp,
                            maxLines = if (descriptionExpanded) Int.MAX_VALUE else 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            
            // Info Line
            item {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = Muted, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(R.string.pack_info), color = Muted, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Tag Chips
            item {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    p.tags.forEach { tag ->
                        Box(
                            modifier = Modifier
                                .background(Surface, RoundedCornerShape(8.dp))
                                .border(1.dp, Line, RoundedCornerShape(8.dp))
                                .clickable { onCategoryClick(tag) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(text = tag, color = Ink, fontSize = 12.sp)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            // Sticker Grid
            item {
                // Since we are in a LazyColumn, we shouldn't use LazyVerticalGrid directly without fixed height. 
                // We can just chunk the items and use Rows.
                val chunks = stickers.chunked(4)
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    chunks.forEach { rowStickers ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            rowStickers.forEach { sticker ->
                                Box(modifier = Modifier.weight(1f)) {
                                    StickerTile(
                                        sticker = sticker,
                                        artistAccentHex = artist?.accentColorHex ?: "#EE5A1C",
                                        onClick = { selectedSticker = sticker }
                                    )
                                }
                            }
                            // Fill empty spaces
                            for (i in 0 until (4 - rowStickers.size)) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
            
            // Copyright
            item {
                Text(
                    text = stringResource(R.string.copyright, p.artistNameAr),
                    color = Muted,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
            
            // Rails
            if (moreByArtist.isNotEmpty()) {
                item {
                    PackRail(
                        title = stringResource(R.string.more_by_artist, p.artistNameAr),
                        packs = moreByArtist,
                        onPackClick = onPackClick
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
            
            if (similarPacks.isNotEmpty()) {
                item {
                    PackRail(
                        title = stringResource(R.string.similar_packs),
                        packs = similarPacks,
                        onPackClick = onPackClick
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
            
            item {
                Text(
                    text = stringResource(R.string.report_pack),
                    color = Muted,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().clickable { /* no-op */ }.padding(16.dp)
                )
            }
        }
    }
    
    // Sticker Sheet
    if (selectedSticker != null) {
        val s = selectedSticker!!
        AlertDialog(
            onDismissRequest = { selectedSticker = null },
            confirmButton = {},
            containerColor = Surface,
            title = null,
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AsyncImage(
                        model = "file:///android_asset/stickers/${s.filePath}",
                        contentDescription = s.captionAr,
                        modifier = Modifier.size(200.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = s.captionAr, fontSize = 16.sp, color = Ink, fontFamily = ChangaFamily, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { 
                            viewModel.shareSticker(s.id) 
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_STREAM, android.net.Uri.parse("file:///android_asset/stickers/${s.filePath}"))
                                type = "image/webp"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, null))
                            selectedSticker = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Ink, contentColor = Surface),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Text(stringResource(R.string.share_sticker), fontFamily = ChangaFamily, fontWeight = FontWeight.Bold)
                    }
                }
            }
        )
    }
}
