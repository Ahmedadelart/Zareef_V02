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
