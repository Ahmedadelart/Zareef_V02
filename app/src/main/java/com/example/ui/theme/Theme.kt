package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val CustomColorScheme = lightColorScheme(
    primary = Accent,
    onPrimary = Surface,
    background = Paper,
    onBackground = Ink,
    surface = Surface,
    onSurface = Ink,
    onSurfaceVariant = Muted,
    outlineVariant = Line,
    error = Heart
)

@Composable
fun ZareefTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(colorScheme = CustomColorScheme, typography = Typography, content = content)
}
