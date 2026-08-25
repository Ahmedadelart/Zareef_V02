package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.example.data.model.Sticker
import com.example.ui.theme.ChangaFamily
import com.example.ui.theme.Muted
import com.example.ui.theme.Surface

@Composable
fun StickerTile(
    sticker: Sticker,
    artistAccentHex: String = "#EE5A1C",
    onClick: () -> Unit
) {
    var isError by remember { mutableStateOf(false) }
    val color = try { Color(android.graphics.Color.parseColor(artistAccentHex)) } catch(e: Exception) { Color.Gray }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isError) color else Surface),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = "file:///android_asset/stickers/${sticker.filePath}",
                contentDescription = sticker.captionAr,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                onState = { state ->
                    isError = state is AsyncImagePainter.State.Error
                }
            )
            
            if (isError) {
                Text(
                    text = sticker.captionAr,
                    color = Surface,
                    fontSize = 12.sp,
                    fontFamily = ChangaFamily,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = sticker.captionAr,
            color = Muted,
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}
