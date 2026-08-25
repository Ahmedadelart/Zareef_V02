package com.example.ui.gift

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.PurchaseHandler
import com.example.data.model.Pack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RedeemViewModel(private val purchaseHandler: PurchaseHandler) : ViewModel() {
    private val _code = MutableStateFlow("")
    val code: StateFlow<String> = _code.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _successPack = MutableStateFlow<Pack?>(null)
    val successPack: StateFlow<Pack?> = _successPack.asStateFlow()

    fun updateCode(newCode: String) {
        _code.value = newCode
        _error.value = null
    }

    fun redeem() {
        val currentCode = _code.value
        if (currentCode.isBlank()) {
            _error.value = "دخل الكود الأول"
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            val result = purchaseHandler.redeem(currentCode)
            result.onSuccess { pack ->
                _successPack.value = pack
            }.onFailure { exception ->
                _error.value = exception.message ?: "الكود ده مش صح أو مستخدم قبل كده"
            }
            
            _isLoading.value = false
        }
    }

    companion object {
        fun provideFactory(purchaseHandler: PurchaseHandler): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return RedeemViewModel(purchaseHandler) as T
                }
            }
    }
}
