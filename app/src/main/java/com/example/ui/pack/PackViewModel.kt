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

    fun addToWhatsApp(context: android.content.Context) {
        _pack.value?.let { p ->
            analyticsTracker.packAddedToWhatsApp(p.id)
            val intent = android.content.Intent().apply {
                action = "com.whatsapp.intent.action.ENABLE_STICKER_PACK"
                putExtra("sticker_pack_id", p.identifier)
                putExtra("sticker_pack_authority", "com.aistudio.zareef.kxmpzq.provider.StickerContentProvider")
                putExtra("sticker_pack_name", p.titleAr)
            }
            try {
                context.startActivity(intent)
            } catch (e: Exception) {
                // WhatsApp not installed
                android.widget.Toast.makeText(context, "واتساب مش متثبت", android.widget.Toast.LENGTH_SHORT).show()
            }
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
