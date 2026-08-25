#!/bin/bash
cat << 'KOTLIN' > app/src/main/java/com/example/ui/shop/ShopViewModel.kt
package com.example.ui.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.StickerRepository
import com.example.data.local.UserPrefs
import com.example.data.model.Artist
import com.example.data.model.Category
import com.example.data.model.Pack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShopViewModel(
    private val repository: StickerRepository,
    private val userPrefs: UserPrefs
) : ViewModel() {
    val packs = repository.getPacks()
    val artists = MutableStateFlow<List<Artist>>(emptyList())
    val categories = repository.getCategories()
    val recentlyViewed = userPrefs.recentlyViewed

    init {
        viewModelScope.launch {
            repository.getPacks().collect { allPacks ->
                val artistIds = allPacks.map { it.artistId }.distinct()
                val artistList = mutableListOf<Artist>()
                artistIds.forEach { id ->
                    repository.getArtist(id).collect { artist ->
                        if (artist != null && !artistList.any { it.id == artist.id }) {
                            artistList.add(artist)
                        }
                    }
                }
                artists.value = artistList.sortedByDescending { it.totalDownloads }
            }
        }
    }

    companion object {
        fun provideFactory(repository: StickerRepository, userPrefs: UserPrefs): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ShopViewModel(repository, userPrefs) as T
                }
            }
    }
}
KOTLIN

cat << 'KOTLIN' > app/src/main/java/com/example/ui/pack/PackViewModel.kt
package com.example.ui.pack

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.AnalyticsTracker
import com.example.data.PurchaseHandler
import com.example.data.StickerRepository
import com.example.data.local.UserPrefs
import com.example.data.model.Artist
import com.example.data.model.Pack
import com.example.data.model.Sticker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PackViewModel(
    private val repository: StickerRepository,
    private val userPrefs: UserPrefs,
    private val purchaseHandler: PurchaseHandler,
    private val analyticsTracker: AnalyticsTracker,
    private val slug: String
) : ViewModel() {
    private val _pack = MutableStateFlow<Pack?>(null)
    val pack: StateFlow<Pack?> = _pack.asStateFlow()

    private val _artist = MutableStateFlow<Artist?>(null)
    val artist: StateFlow<Artist?> = _artist.asStateFlow()

    private val _stickers = MutableStateFlow<List<Sticker>>(emptyList())
    val stickers: StateFlow<List<Sticker>> = _stickers.asStateFlow()

    private val _similarPacks = MutableStateFlow<List<Pack>>(emptyList())
    val similarPacks: StateFlow<List<Pack>> = _similarPacks.asStateFlow()

    private val _moreByArtist = MutableStateFlow<List<Pack>>(emptyList())
    val moreByArtist: StateFlow<List<Pack>> = _moreByArtist.asStateFlow()

    val recentlyViewed = userPrefs.recentlyViewed
    val favourites = userPrefs.favourites

    init {
        viewModelScope.launch {
            repository.getPackBySlug(slug).collectLatest { p ->
                _pack.value = p
                if (p != null) {
                    analyticsTracker.packViewed(p.id)
                    userPrefs.addRecentlyViewed(p.id)
                    
                    repository.getArtist(p.artistId).collectLatest { a ->
                        _artist.value = a
                    }
                    repository.getStickersForPack(p.id).collectLatest { s ->
                        _stickers.value = s
                    }
                    repository.getPacks().collectLatest { allPacks ->
                        _moreByArtist.value = allPacks.filter { it.artistId == p.artistId && it.id != p.id }
                        _similarPacks.value = allPacks.filter { it.id != p.id && it.tags.intersect(p.tags.toSet()).isNotEmpty() }.take(6)
                    }
                }
            }
        }
    }

    fun toggleFavourite() {
        _pack.value?.let { p ->
            viewModelScope.launch {
                userPrefs.toggleFavourite(p.id)
            }
        }
    }

    fun buyPack() {
        _pack.value?.let { p ->
            viewModelScope.launch {
                purchaseHandler.purchase(p.id)
            }
        }
    }

    fun giftPack() {
        _pack.value?.let { p ->
            viewModelScope.launch {
                val result = purchaseHandler.purchaseAsGift(p.id)
                result.onSuccess { gift ->
                    analyticsTracker.giftSent(p.id)
                }
            }
        }
    }

    fun shareSticker(stickerId: String) {
        _pack.value?.let { p ->
            analyticsTracker.stickerShared(p.id, stickerId)
        }
    }

    fun addToWhatsApp() {
        _pack.value?.let { p ->
            analyticsTracker.packAddedToWhatsApp(p.id)
        }
    }

    companion object {
        fun provideFactory(
            repository: StickerRepository,
            userPrefs: UserPrefs,
            purchaseHandler: PurchaseHandler,
            analyticsTracker: AnalyticsTracker,
            slug: String
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PackViewModel(repository, userPrefs, purchaseHandler, analyticsTracker, slug) as T
                }
            }
    }
}
KOTLIN

cat << 'KOTLIN' > app/src/main/java/com/example/ui/artist/ArtistViewModel.kt
package com.example.ui.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.AnalyticsTracker
import com.example.data.StickerRepository
import com.example.data.model.Artist
import com.example.data.model.Pack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ArtistViewModel(
    private val repository: StickerRepository,
    private val analyticsTracker: AnalyticsTracker,
    private val artistId: String
) : ViewModel() {
    private val _artist = MutableStateFlow<Artist?>(null)
    val artist: StateFlow<Artist?> = _artist.asStateFlow()

    private val _packs = MutableStateFlow<List<Pack>>(emptyList())
    val packs: StateFlow<List<Pack>> = _packs.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getArtist(artistId).collectLatest { a ->
                _artist.value = a
            }
        }
        viewModelScope.launch {
            repository.getPacks().collectLatest { allPacks ->
                _packs.value = allPacks.filter { it.artistId == artistId }
            }
        }
    }
    
    fun openSocial(network: String) {
        analyticsTracker.artistSocialOpened(artistId, network)
    }

    companion object {
        fun provideFactory(repository: StickerRepository, analyticsTracker: AnalyticsTracker, artistId: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ArtistViewModel(repository, analyticsTracker, artistId) as T
                }
            }
    }
}
KOTLIN

cat << 'KOTLIN' > app/src/main/java/com/example/ui/category/CategoryViewModel.kt
package com.example.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.StickerRepository
import com.example.data.model.Pack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val repository: StickerRepository,
    private val categoryId: String
) : ViewModel() {
    private val _packs = MutableStateFlow<List<Pack>>(emptyList())
    val packs: StateFlow<List<Pack>> = _packs.asStateFlow()
    
    val categoryName = MutableStateFlow("")

    init {
        viewModelScope.launch {
            repository.getCategories().collectLatest { cats ->
                val cat = cats.find { it.id == categoryId }
                if (cat != null) {
                    categoryName.value = cat.nameAr
                }
            }
        }
        viewModelScope.launch {
            repository.getPacks().collectLatest { allPacks ->
                val filtered = if (categoryId.startsWith("type_")) {
                    allPacks.filter { it.type == categoryId.removePrefix("type_") }
                } else if (categoryId.startsWith("dialect_")) {
                    allPacks.filter { it.dialect == categoryId.removePrefix("dialect_") }
                } else {
                    allPacks.filter { it.tags.contains(categoryId) }
                }
                _packs.value = filtered
            }
        }
    }

    companion object {
        fun provideFactory(repository: StickerRepository, categoryId: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return CategoryViewModel(repository, categoryId) as T
                }
            }
    }
}
KOTLIN

cat << 'KOTLIN' > app/src/main/java/com/example/ui/search/SearchViewModel.kt
package com.example.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.AnalyticsTracker
import com.example.data.StickerRepository
import com.example.data.model.Pack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: StickerRepository,
    private val analyticsTracker: AnalyticsTracker
) : ViewModel() {
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _results = MutableStateFlow<List<Pack>>(emptyList())
    val results: StateFlow<List<Pack>> = _results.asStateFlow()

    private val allPacks = MutableStateFlow<List<Pack>>(emptyList())
    
    val popularSearches = listOf("قطط", "شغل", "يومي", "حب")
    val popularEmojis = listOf("😂", "😭", "❤️", "🥺", "😡")

    init {
        viewModelScope.launch {
            repository.getPacks().collectLatest { packs ->
                allPacks.value = packs
                updateResults()
            }
        }
    }

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
        updateResults()
    }

    fun performSearch() {
        if (query.value.isNotBlank()) {
            analyticsTracker.searchPerformed(query.value)
        }
    }
    
    fun setFilter(filter: String) {
        // Simple implementation for filter chips overriding query
        _query.value = filter
        updateResults()
        performSearch()
    }

    private fun updateResults() {
        val q = _query.value.trim().lowercase()
        if (q.isEmpty()) {
            _results.value = emptyList()
            return
        }
        
        // Handle specific filters
        if (q == "متحرك") {
            _results.value = allPacks.value.filter { it.type == "animated" }
            return
        }
        if (q == "مجاني") {
            _results.value = allPacks.value.filter { it.isFree }
            return
        }
        if (q == "مصري") {
            _results.value = allPacks.value.filter { it.dialect == "egyptian" }
            return
        }
        if (q == "خليجي") {
            _results.value = allPacks.value.filter { it.dialect == "gulf" }
            return
        }
        if (q == "ثابت") {
            _results.value = allPacks.value.filter { it.type == "static" }
            return
        }

        _results.value = allPacks.value.filter { pack ->
            pack.titleAr.lowercase().contains(q) || 
            pack.artistNameAr.lowercase().contains(q) ||
            pack.tags.any { it.lowercase().contains(q) } ||
            pack.keywords.any { it.lowercase().contains(q) }
        }
    }

    companion object {
        fun provideFactory(repository: StickerRepository, analyticsTracker: AnalyticsTracker): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel(repository, analyticsTracker) as T
                }
            }
    }
}
KOTLIN

cat << 'KOTLIN' > app/src/main/java/com/example/ui/account/AccountViewModel.kt
package com.example.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.AuthHandler
import com.example.data.StickerRepository
import com.example.data.local.UserPrefs
import com.example.data.model.Pack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AccountViewModel(
    private val authHandler: AuthHandler,
    private val userPrefs: UserPrefs,
    private val repository: StickerRepository
) : ViewModel() {
    val currentUser = authHandler.currentUser
    
    private val _favouritePacks = MutableStateFlow<List<Pack>>(emptyList())
    val favouritePacks: StateFlow<List<Pack>> = _favouritePacks.asStateFlow()

    init {
        viewModelScope.launch {
            userPrefs.favourites.collectLatest { favIds ->
                repository.getPacks().collectLatest { allPacks ->
                    _favouritePacks.value = allPacks.filter { favIds.contains(it.id) }
                }
            }
        }
    }

    fun signIn() {
        viewModelScope.launch {
            authHandler.signInWithGoogle()
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authHandler.signOut()
        }
    }

    companion object {
        fun provideFactory(authHandler: AuthHandler, userPrefs: UserPrefs, repository: StickerRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AccountViewModel(authHandler, userPrefs, repository) as T
                }
            }
    }
}
KOTLIN
