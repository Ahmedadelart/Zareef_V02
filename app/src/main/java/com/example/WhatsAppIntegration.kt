package com.example

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.content.res.AssetFileDescriptor
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.util.Log
import com.example.data.model.Catalog
import com.example.data.model.Pack
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class StickerContentProvider : ContentProvider() {

    private lateinit var catalog: Catalog

    companion object {
        const val AUTHORITY = "com.aistudio.zareef.kxmpzq.provider.StickerContentProvider"
        val MATCHER = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "metadata", 1)
            addURI(AUTHORITY, "metadata/*", 2)
            addURI(AUTHORITY, "stickers/*", 3)
            addURI(AUTHORITY, "stickers_asset/*/*", 4)
        }
    }

    override fun onCreate(): Boolean {
        try {
            val json = context?.assets?.open("catalog.json")?.bufferedReader()?.use { it.readText() } ?: return false
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            catalog = moshi.adapter(Catalog::class.java).fromJson(json) ?: return false
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val code = MATCHER.match(uri)
        return when (code) {
            1 -> getPackList()
            2 -> getPackList(uri.lastPathSegment!!)
            3 -> getStickers(uri.lastPathSegment!!)
            else -> null
        }
    }

    private fun getPackList(identifier: String? = null): Cursor {
        val cursor = MatrixCursor(arrayOf(
            "sticker_pack_identifier", "sticker_pack_name", "sticker_pack_publisher",
            "sticker_pack_icon", "android_play_store_link", "ios_app_store_link",
            "publisher_email", "publisher_website", "privacy_policy_website",
            "license_agreement_website", "image_data_version", "avoid_cache", "animated_sticker_pack"
        ))
        
        val packs = if (identifier != null) catalog.packs.filter { it.identifier == identifier } else catalog.packs
        for (pack in packs) {
            cursor.addRow(arrayOf(
                pack.identifier, pack.titleAr, pack.artistNameAr,
                pack.trayIconPath, "", "", "", "", "", "", pack.imageDataVersion, 0, if(pack.type == "animated") 1 else 0
            ))
        }
        return cursor
    }

    private fun getStickers(identifier: String): Cursor {
        val cursor = MatrixCursor(arrayOf("sticker_file_name", "sticker_emoji"))
        val pack = catalog.packs.find { it.identifier == identifier } ?: return cursor
        val stickers = catalog.stickers.filter { it.packId == pack.id }
        
        for (sticker in stickers) {
            cursor.addRow(arrayOf(sticker.filePath, sticker.emojis.joinToString(",")))
        }
        return cursor
    }

    override fun openAssetFile(uri: Uri, mode: String): AssetFileDescriptor? {
        val matchCode = MATCHER.match(uri)
        if (matchCode == 4) {
            val identifier = uri.pathSegments[1]
            val fileName = uri.pathSegments[2]
            return fetchFile(identifier, fileName)
        }
        return null
    }

    private fun fetchFile(identifier: String, fileName: String): AssetFileDescriptor? {
        return try {
            context?.assets?.openFd("stickers/$fileName")
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun getType(uri: Uri): String? {
        val matchCode = MATCHER.match(uri)
        return when (matchCode) {
            1 -> "vnd.android.cursor.dir/vnd.com.aistudio.zareef.kxmpzq.provider.StickerContentProvider.metadata"
            2 -> "vnd.android.cursor.item/vnd.com.aistudio.zareef.kxmpzq.provider.StickerContentProvider.metadata"
            3 -> "vnd.android.cursor.dir/vnd.com.aistudio.zareef.kxmpzq.provider.StickerContentProvider.stickers"
            4 -> "image/webp"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? = throw UnsupportedOperationException()
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = throw UnsupportedOperationException()
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = throw UnsupportedOperationException()
}
