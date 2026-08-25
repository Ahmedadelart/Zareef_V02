package com.example.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("user_prefs")

class UserPrefs(private val context: Context) {
    private val RECENTLY_VIEWED = stringPreferencesKey("recently_viewed")
    private val FAVOURITES = stringPreferencesKey("favourites")
    private val OWNED_PACKS = stringPreferencesKey("owned_packs")

    val recentlyViewed: Flow<List<String>> = context.dataStore.data.map { prefs ->
        prefs[RECENTLY_VIEWED]?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()
    }

    suspend fun addRecentlyViewed(packId: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[RECENTLY_VIEWED]?.split(",")?.filter { it.isNotEmpty() }?.toMutableList() ?: mutableListOf()
            current.remove(packId)
            current.add(0, packId)
            if (current.size > 10) {
                current.removeAt(10)
            }
            prefs[RECENTLY_VIEWED] = current.joinToString(",")
        }
    }

    val favourites: Flow<List<String>> = context.dataStore.data.map { prefs ->
        prefs[FAVOURITES]?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()
    }

    suspend fun toggleFavourite(packId: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[FAVOURITES]?.split(",")?.filter { it.isNotEmpty() }?.toMutableList() ?: mutableListOf()
            if (current.contains(packId)) {
                current.remove(packId)
            } else {
                current.add(packId)
            }
            prefs[FAVOURITES] = current.joinToString(",")
        }
    }

    val ownedPacks: Flow<List<String>> = context.dataStore.data.map { prefs ->
        prefs[OWNED_PACKS]?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()
    }

    suspend fun addOwnedPack(packId: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[OWNED_PACKS]?.split(",")?.filter { it.isNotEmpty() }?.toMutableList() ?: mutableListOf()
            if (!current.contains(packId)) {
                current.add(packId)
                prefs[OWNED_PACKS] = current.joinToString(",")
            }
        }
    }
}
