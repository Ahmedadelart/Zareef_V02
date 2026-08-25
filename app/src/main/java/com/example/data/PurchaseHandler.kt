package com.example.data

import com.example.data.model.Gift
import com.example.data.model.Pack

interface PurchaseHandler {
    suspend fun purchase(packId: String): Result<Unit>
    suspend fun purchaseAsGift(packId: String): Result<Gift>
    suspend fun redeem(code: String): Result<Pack>
}
