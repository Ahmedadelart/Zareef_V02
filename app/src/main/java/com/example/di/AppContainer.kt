package com.example.di

import android.content.Context
import com.example.data.AnalyticsTracker
import com.example.data.AuthHandler
import com.example.data.PurchaseHandler
import com.example.data.StickerRepository
import com.example.data.local.LocalAuthHandler
import com.example.data.local.LocalPurchaseHandler
import com.example.data.local.LocalStickerRepository
import com.example.data.local.LogAnalyticsTracker
import com.example.data.local.UserPrefs

class AppContainer(private val context: Context) {
    val userPrefs: UserPrefs by lazy { UserPrefs(context) }
    
    // Switch implementations here when adding Firebase / Play Billing
    val stickerRepository: StickerRepository by lazy { LocalStickerRepository(context) }
    val purchaseHandler: PurchaseHandler by lazy { LocalPurchaseHandler(userPrefs) }
    val authHandler: AuthHandler by lazy { LocalAuthHandler() }
    val analyticsTracker: AnalyticsTracker by lazy { LogAnalyticsTracker() }
}
