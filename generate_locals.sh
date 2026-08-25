#!/bin/bash
cat << 'KOTLIN' > app/src/main/java/com/example/data/local/LocalAuthHandler.kt
package com.example.data.local

import com.example.data.AuthHandler
import com.example.data.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocalAuthHandler : AuthHandler {
    private val _currentUser = MutableStateFlow<User?>(null)
    override val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    override suspend fun signInWithGoogle(): Result<User> {
        delay(600)
        val fakeUser = User(uid = "test_uid", displayName = "Zareef User", email = "hello@zareef.app", photoUrl = "", isAnonymous = false)
        _currentUser.value = fakeUser
        return Result.success(fakeUser)
    }

    override suspend fun signOut() {
        _currentUser.value = null
    }

    override suspend fun deleteAccount(): Result<Unit> {
        _currentUser.value = null
        return Result.success(Unit)
    }
}
KOTLIN

cat << 'KOTLIN' > app/src/main/java/com/example/data/local/LocalPurchaseHandler.kt
package com.example.data.local

import com.example.data.PurchaseHandler
import com.example.data.model.Gift
import com.example.data.model.Pack
import kotlinx.coroutines.delay
import java.util.UUID

class LocalPurchaseHandler : PurchaseHandler {
    override suspend fun purchase(packId: String): Result<Unit> {
        delay(1000)
        return Result.success(Unit)
    }

    override suspend fun purchaseAsGift(packId: String): Result<Gift> {
        delay(1000)
        val code = "ZRF-\${UUID.randomUUID().toString().substring(0,4).uppercase()}-\${UUID.randomUUID().toString().substring(0,4).uppercase()}"
        val gift = Gift(
            id = UUID.randomUUID().toString(),
            packId = packId,
            code = code,
            status = "pending",
            createdAt = System.currentTimeMillis(),
            redeemedAt = null
        )
        return Result.success(gift)
    }

    override suspend fun redeem(code: String): Result<Pack> {
        delay(1000)
        return Result.failure(Exception("Not implemented in local mock"))
    }
}
KOTLIN

cat << 'KOTLIN' > app/src/main/java/com/example/data/local/LogAnalyticsTracker.kt
package com.example.data.local

import android.util.Log
import com.example.data.AnalyticsTracker

class LogAnalyticsTracker : AnalyticsTracker {
    private val TAG = "ZareefAnalytics"
    
    override fun packViewed(packId: String) {
        Log.d(TAG, "packViewed: \$packId")
    }

    override fun packAddedToWhatsApp(packId: String) {
        Log.d(TAG, "packAddedToWhatsApp: \$packId")
    }

    override fun stickerShared(packId: String, stickerId: String) {
        Log.d(TAG, "stickerShared: \$packId, \$stickerId")
    }

    override fun giftSent(packId: String) {
        Log.d(TAG, "giftSent: \$packId")
    }

    override fun searchPerformed(query: String) {
        Log.d(TAG, "searchPerformed: \$query")
    }

    override fun artistSocialOpened(artistId: String, network: String) {
        Log.d(TAG, "artistSocialOpened: \$artistId, \$network")
    }
}
KOTLIN

