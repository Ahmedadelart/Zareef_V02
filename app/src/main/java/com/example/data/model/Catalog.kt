package com.example.data.model
import kotlinx.serialization.Serializable

@Serializable
data class Catalog(
    val packs: List<Pack>,
    val artists: List<Artist>,
    val stickers: List<Sticker>
)
