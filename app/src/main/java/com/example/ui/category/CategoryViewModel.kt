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
