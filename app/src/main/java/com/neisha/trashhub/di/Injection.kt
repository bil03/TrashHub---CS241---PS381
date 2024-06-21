package com.neisha.trashhub.di

import android.content.Context
import com.neisha.trashhub.data.UserRepository
import com.neisha.trashhub.data.pref.UserPreference
import com.neisha.trashhub.data.pref.dataStore
import com.neisha.trashhub.data.repository.ArticlesRepository
import com.neisha.trashhub.data.retrofit.ApiConfig

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val authApiService = ApiConfig.getApiServiceWithoutToken()
        return UserRepository.getInstance(pref, authApiService)
    }

    fun provideArticlesRepository(): ArticlesRepository {
        val apiService = ApiConfig.getApiServiceWithoutToken()
        return ArticlesRepository(apiService)
    }
}