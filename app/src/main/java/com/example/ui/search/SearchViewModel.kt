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
