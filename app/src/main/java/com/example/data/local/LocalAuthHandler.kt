package com.example.data.local

import com.example.data.AuthHandler
import com.example.data.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocalAuthHandler : AuthHandler {
    private val _currentUser = MutableStateFlow<User?>(null)
    override val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    override suspend fun signInWithGoogle(): Result<User> {
        delay(600)
        val fakeUser = User(uid = "test_uid", displayName = "Zareef User", email = "hello@zareef.app", photoUrl = "", isAnonymous = false)
        _currentUser.value = fakeUser
        return Result.success(fakeUser)
    }

    override suspend fun signOut() {
        _currentUser.value = null
    }

    override suspend fun deleteAccount(): Result<Unit> {
        _currentUser.value = null
        return Result.success(Unit)
    }
}
