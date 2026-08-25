package com.example.data.model

data class Gift(
    val id: String, val packId: String, val code: String,
    val status: String,              // "pending" | "redeemed"
    val createdAt: Long, val redeemedAt: Long?
)
