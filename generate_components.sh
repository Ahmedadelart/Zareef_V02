#!/bin/bash
mkdir -p app/src/main/java/com/example/ui/components

cat << 'KOTLIN' > app/src/main/java/com/example/ui/components/ZareefLogo.kt
package com.example.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Accent
import com.example.ui.theme.ChangaFamily

@Composable
fun ZareefLogo(modifier: Modifier = Modifier) {
    Text(
        text = "ظريف",
        fontFamily = ChangaFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 28.sp,
        color = Accent,
        modifier = modifier
    )
}
KOTLIN

cat << 'KOTLIN' > app/src/main/java/com/example/ui/components/SectionHeader.kt
package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.ChangaFamily
import com.example.ui.theme.Ink
import com.example.ui.theme.Muted

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    onViewAllClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontFamily = ChangaFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Ink
        )
        if (onViewAllClick != null) {
            Text(
                text = stringResource(R.string.view_all),
                fontSize = 12.sp,
                color = Muted,
                modifier = Modifier.clickable(onClick = onViewAllClick)
            )
        }
    }
}
KOTLIN

cat << 'KOTLIN' > app/src/main/java/com/example/ui/components/PackCard.kt
package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.model.Pack
import com.example.ui.theme.ChangaFamily
import com.example.ui.theme.Ink
import com.example.ui.theme.Muted
import com.example.ui.theme.Surface

@Composable
fun PackCard(
    pack: Pack,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(Surface)
        ) {
            AsyncImage(
                model = "file:///android_asset/stickers/\${pack.trayIconPath}",
                contentDescription = pack.titleAr,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            if (pack.isEditorsPick) {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .background(com.example.ui.theme.Accent, RoundedCornerShape(8.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = stringResource(R.string.editor_pick),
                        color = Surface,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = ChangaFamily
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = pack.titleAr,
            color = Ink,
            fontFamily = ChangaFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = pack.artistNameAr,
            color = Muted,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
KOTLIN

cat << 'KOTLIN' > app/src/main/java/com/example/ui/components/PackRail.kt
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
KOTLIN

cat << 'KOTLIN' > app/src/main/java/com/example/ui/components/ArtistCircle.kt
package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Artist
import com.example.ui.theme.ChangaFamily
import com.example.ui.theme.Ink
import com.example.ui.theme.Muted
import com.example.ui.theme.Surface

@Composable
fun ArtistCircle(
    artist: Artist,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val color = try { Color(android.graphics.Color.parseColor(artist.accentColorHex)) } catch (e: Exception) { Ink }
    Column(
        modifier = modifier
            .width(72.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = artist.avatarInitial,
                color = Surface,
                fontFamily = ChangaFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = artist.nameAr,
            color = Ink,
            fontFamily = ChangaFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "\${artist.totalDownloads}",
            color = Muted,
            fontSize = 10.sp,
            maxLines = 1
        )
    }
}
KOTLIN

