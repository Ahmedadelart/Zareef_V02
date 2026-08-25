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
