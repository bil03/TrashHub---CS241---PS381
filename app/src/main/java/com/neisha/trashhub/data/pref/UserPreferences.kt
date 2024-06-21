package com.neisha.trashhub.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = user.id
            preferences[EMAIL_KEY] = user.email
            preferences[NAME_KEY] = user.name
            preferences[TOKEN_KEY] = user.token
            preferences[PHONE_KEY] = user.phone
            preferences[ROLES_KEY] = user.roles
            preferences[IS_LOGIN_KEY] = user.isLogin
        }
    }

    fun getSession(): Flow<UserModel?> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                if (preferences[USER_ID_KEY] != null) {
                    UserModel(
                        id = preferences[USER_ID_KEY] ?: "",
                        name = preferences[NAME_KEY] ?: "",
                        email = preferences[EMAIL_KEY] ?: "",
                        token = preferences[TOKEN_KEY] ?: "",
                        phone = preferences[PHONE_KEY] ?: "",
                        roles = preferences[ROLES_KEY] ?: "",
                        isLogin = preferences[IS_LOGIN_KEY] ?: false
                    )
                } else {
                    null
                }
            }
    }

    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.remove(USER_ID_KEY)
            preferences.remove(NAME_KEY)
            preferences.remove(EMAIL_KEY)
            preferences.remove(TOKEN_KEY)
            preferences.remove(PHONE_KEY)
            preferences.remove(ROLES_KEY)
            preferences[IS_LOGIN_KEY] = false
        }
    }

    suspend fun getToken(): String? {
        return getSession().map { it?.token }.first()
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val PHONE_KEY = stringPreferencesKey("phone")
        private val ROLES_KEY = stringPreferencesKey("roles")
        private val IS_LOGIN_KEY = booleanPreferencesKey("is_login")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
