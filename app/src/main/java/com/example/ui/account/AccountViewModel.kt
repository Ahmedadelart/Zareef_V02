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

    private val _ownedPacks = MutableStateFlow<List<Pack>>(emptyList())
    val ownedPacks: StateFlow<List<Pack>> = _ownedPacks.asStateFlow()

    init {
        viewModelScope.launch {
            userPrefs.favourites.collectLatest { favIds ->
                repository.getPacks().collectLatest { allPacks ->
                    _favouritePacks.value = allPacks.filter { favIds.contains(it.id) }
                }
            }
        }
        viewModelScope.launch {
            userPrefs.ownedPacks.collectLatest { ownedIds ->
                repository.getPacks().collectLatest { allPacks ->
                    _ownedPacks.value = allPacks.filter { ownedIds.contains(it.id) }
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
