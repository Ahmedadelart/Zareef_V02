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
