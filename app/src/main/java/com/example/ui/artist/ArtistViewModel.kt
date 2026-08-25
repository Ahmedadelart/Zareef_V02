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
