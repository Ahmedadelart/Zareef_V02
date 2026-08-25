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
                    text = "${index + 1}",
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
                        model = "file:///android_asset/stickers/${pack.trayIconPath}",
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
