package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Pack(
    val id: String,
    val slug: String,
    val identifier: String,          // WhatsApp pack id — permanent, never regenerate
    val titleAr: String,
    val titleEn: String,
    val artistId: String,
    val artistNameAr: String,
    val characterId: String,
    val characterNameAr: String,
    val type: String,                // "static" | "animated"
    val dialect: String,             // "egyptian" | "gulf" | "levantine" | "msa"
    val tags: List<String>,
    val isFree: Boolean,
    val priceEGP: Int,
    val stickerCount: Int,
    val trayIconPath: String,
    val previewPaths: List<String>,
    val keywords: List<String>,
    val descriptionAr: String,       // MAY BE EMPTY
    val isOwnIP: Boolean,            // true = a Big Cat character, drives "شخصياتنا"
    val isEditorsPick: Boolean,      // drives "اختياراتنا"
    val downloads: Int,
    val favourites: Int,
    val publishedAt: Long,
    val imageDataVersion: Int
)
