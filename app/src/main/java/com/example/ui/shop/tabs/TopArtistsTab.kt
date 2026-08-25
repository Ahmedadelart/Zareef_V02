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
                    text = "${index + 1}",
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
                        text = "${artist.totalDownloads} تنزيل",
                        color = Muted,
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
