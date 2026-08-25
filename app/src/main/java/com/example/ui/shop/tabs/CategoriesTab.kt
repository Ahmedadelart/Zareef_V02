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
                            model = "file:///android_asset/stickers/${previewPack.trayIconPath}",
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
                    text = "(${category.packCount})",
                    fontSize = 14.sp,
                    color = Muted
                )
            }
        }
    }
}
