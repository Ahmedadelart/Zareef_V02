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
