#!/bin/bash
cat << 'KOTLIN' > app/src/main/java/com/example/data/model/Sticker.kt
package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Sticker(
    val id: String, 
    val packId: String, 
    val index: Int,
    val filePath: String,
    val emojis: List<String>,        // 1-3, required by WhatsApp
    val captionAr: String
)
KOTLIN

cat << 'KOTLIN' > app/src/main/java/com/example/data/model/Artist.kt
package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Artist(
    val id: String, val nameAr: String, val handle: String,
    val bioAr: String,               // MAY BE EMPTY — hide when blank
    val avatarInitial: String,
    val accentColorHex: String,
    val instagram: String, val tiktok: String, val behance: String,
    val totalDownloads: Int
)
KOTLIN

cat << 'KOTLIN' > app/src/main/java/com/example/data/model/Gift.kt
package com.example.data.model

data class Gift(
    val id: String, val packId: String, val code: String,
    val status: String,              // "pending" | "redeemed"
    val createdAt: Long, val redeemedAt: Long?
)
KOTLIN

cat << 'KOTLIN' > app/src/main/java/com/example/data/model/Category.kt
package com.example.data.model

data class Category(
    val id: String, val nameAr: String, val nameEn: String,
    val packCount: Int, val previewPackId: String
)
KOTLIN

cat << 'KOTLIN' > app/src/main/java/com/example/data/model/User.kt
package com.example.data.model

data class User(
    val uid: String, val displayName: String, val email: String,
    val photoUrl: String, val isAnonymous: Boolean
)
KOTLIN

cat << 'KOTLIN' > app/src/main/java/com/example/data/StickerRepository.kt
package com.example.data

import com.example.data.model.Category
import com.example.data.model.Pack
import com.example.data.model.Sticker
import com.example.data.model.Artist
import kotlinx.coroutines.flow.Flow

interface StickerRepository {
    fun getPacks(): Flow<List<Pack>>
    fun getPack(id: String): Flow<Pack?>
    fun getPackBySlug(slug: String): Flow<Pack?>
    fun getStickersForPack(packId: String): Flow<List<Sticker>>
    fun getArtist(artistId: String): Flow<Artist?>
    fun getCategories(): Flow<List<Category>>
    suspend fun refresh()
}
KOTLIN

cat << 'KOTLIN' > app/src/main/java/com/example/data/PurchaseHandler.kt
package com.example.data

import com.example.data.model.Gift
import com.example.data.model.Pack

interface PurchaseHandler {
    suspend fun purchase(packId: String): Result<Unit>
    suspend fun purchaseAsGift(packId: String): Result<Gift>
    suspend fun redeem(code: String): Result<Pack>
}
KOTLIN

cat << 'KOTLIN' > app/src/main/java/com/example/data/AuthHandler.kt
package com.example.data

import com.example.data.model.User
import kotlinx.coroutines.flow.StateFlow

interface AuthHandler {
    val currentUser: StateFlow<User?>
    suspend fun signInWithGoogle(): Result<User>
    suspend fun signOut()
    suspend fun deleteAccount(): Result<Unit>
}
KOTLIN

cat << 'KOTLIN' > app/src/main/java/com/example/data/AnalyticsTracker.kt
package com.example.data

interface AnalyticsTracker {
    fun packViewed(packId: String)
    fun packAddedToWhatsApp(packId: String)
    fun stickerShared(packId: String, stickerId: String)
    fun giftSent(packId: String)
    fun searchPerformed(query: String)
    fun artistSocialOpened(artistId: String, network: String)
}
KOTLIN

