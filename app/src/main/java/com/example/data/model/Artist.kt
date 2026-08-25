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
