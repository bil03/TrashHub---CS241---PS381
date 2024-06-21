package com.neisha.trashhub.data

import com.neisha.trashhub.data.pref.UserModel
import com.neisha.trashhub.data.pref.UserPreference
import com.neisha.trashhub.data.pref.response.LoginResponse
import com.neisha.trashhub.data.pref.response.RegisterResponse
import com.neisha.trashhub.data.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val authApiService: ApiService
) {
    suspend fun register(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        phone: String,
        mitra: String
    ): RegisterResponse {
        return authApiService.register(name, email, password, confirmPassword, phone, mitra)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return authApiService.login(email, password)
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel?> {
        return userPreference.getSession()
    }
    suspend fun logout() {
        userPreference.clearSession()
    }

    suspend fun refreshToken(userId: String): String? {
        val currentSession = userPreference.getSession().firstOrNull()
        val currentToken = currentSession?.token

        // Pastikan token saat ini tidak null sebelum mencoba refresh
        if (currentToken.isNullOrEmpty()) {
            return null
        }

        val response = authApiService.refreshToken("Bearer $currentToken", userId)

        // Cek apakah ada kesalahan dalam respons atau token baru adalah null
        if (response.error == true || response.token.isNullOrEmpty()) {
            return null
        }

        val newToken = response.token

        // Perbarui sesi dengan token baru
        val updatedUser = currentSession.copy(token = newToken)
        saveSession(updatedUser)

        return newToken
    }



    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null

        fun getInstance(userPreference: UserPreference, authApiService: ApiService): UserRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = UserRepository(userPreference, authApiService)
                INSTANCE = instance
                instance
            }
        }
    }
}
