package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.data.model.Pack

@Composable
fun PackRail(
    title: String,
    packs: List<Pack>,
    modifier: Modifier = Modifier,
    onPackClick: (String) -> Unit,
    onViewAllClick: (() -> Unit)? = null
) {
    if (packs.isEmpty()) return
    
    Column(modifier = modifier) {
        SectionHeader(title = title, onViewAllClick = onViewAllClick)
        
        BoxWithConstraints {
            val startPadding = 16.dp
            val gap = 10.dp
            val cardWidth = (maxWidth - startPadding - (gap * 3)) / 3.4f
            
            LazyRow(
                contentPadding = PaddingValues(start = startPadding, end = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(gap)
            ) {
                items(packs) { pack ->
                    PackCard(
                        pack = pack,
                        modifier = Modifier.width(cardWidth),
                        onClick = { onPackClick(pack.slug) }
                    )
                }
            }
        }
    }
}
