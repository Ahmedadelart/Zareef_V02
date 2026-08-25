package com.example.data.local

import com.example.data.PurchaseHandler
import com.example.data.model.Gift
import com.example.data.model.Pack
import kotlinx.coroutines.delay
import java.util.UUID

class LocalPurchaseHandler(private val userPrefs: com.example.data.local.UserPrefs) : PurchaseHandler {
    override suspend fun purchase(packId: String): Result<Unit> {
        delay(1000)
        userPrefs.addOwnedPack(packId)
        return Result.success(Unit)
    }

    override suspend fun purchaseAsGift(packId: String): Result<Gift> {
        delay(1000)
        val code = "ZRF-${UUID.randomUUID().toString().substring(0,4).uppercase()}-${UUID.randomUUID().toString().substring(0,4).uppercase()}"
        val gift = Gift(
            id = UUID.randomUUID().toString(),
            packId = packId,
            code = code,
            status = "pending",
            createdAt = System.currentTimeMillis(),
            redeemedAt = null
        )
        return Result.success(gift)
    }

    override suspend fun redeem(code: String): Result<Pack> {
        delay(1000)
        return Result.failure(Exception("Not implemented in local mock"))
    }
}
