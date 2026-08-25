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
