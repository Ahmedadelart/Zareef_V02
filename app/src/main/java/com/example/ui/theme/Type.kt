package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.example.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val changaFont = GoogleFont("Changa")
val alexandriaFont = GoogleFont("Alexandria")

val ChangaFamily = FontFamily(
    Font(googleFont = changaFont, fontProvider = provider, weight = FontWeight.Bold),
    Font(googleFont = changaFont, fontProvider = provider, weight = FontWeight.ExtraBold)
)

val AlexandriaFamily = FontFamily(
    Font(googleFont = alexandriaFont, fontProvider = provider, weight = FontWeight.Light),
    Font(googleFont = alexandriaFont, fontProvider = provider, weight = FontWeight.Normal)
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = ChangaFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 57.sp,
        lineHeight = 72.sp,
    ),
    displayMedium = TextStyle(
        fontFamily = ChangaFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        lineHeight = 58.sp,
    ),
    displaySmall = TextStyle(
        fontFamily = ChangaFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 48.sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = ChangaFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 44.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = ChangaFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 40.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = ChangaFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 36.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = ChangaFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 34.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = ChangaFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 28.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = ChangaFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 24.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = AlexandriaFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 28.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = AlexandriaFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 24.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = AlexandriaFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 20.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = AlexandriaFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 24.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = AlexandriaFamily,
        fontWeight = FontWeight.Light,
        fontSize = 12.sp,
        lineHeight = 20.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = AlexandriaFamily,
        fontWeight = FontWeight.Light,
        fontSize = 11.sp,
        lineHeight = 20.sp,
    )
)
