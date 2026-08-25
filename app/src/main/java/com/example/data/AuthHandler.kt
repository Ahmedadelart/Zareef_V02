package com.example.data

import com.example.data.model.User
import kotlinx.coroutines.flow.StateFlow

interface AuthHandler {
    val currentUser: StateFlow<User?>
    suspend fun signInWithGoogle(): Result<User>
    suspend fun signOut()
    suspend fun deleteAccount(): Result<Unit>
}
