package com.neisha.trashhub.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.neisha.trashhub.data.UserRepository
import com.neisha.trashhub.data.repository.ArticlesRepository
import com.neisha.trashhub.di.Injection
import com.neisha.trashhub.view.Profile.ProfileViewModel
import com.neisha.trashhub.view.articles.ArticleDetailViewModel
import com.neisha.trashhub.view.articles.ArticlesViewModel

class ViewModelFactory(
    private val userRepository: UserRepository,
    private val articlesRepository: ArticlesRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(ArticlesViewModel::class.java) -> {
                ArticlesViewModel(articlesRepository) as T
            }
            modelClass.isAssignableFrom(ArticleDetailViewModel::class.java) -> {
                ArticleDetailViewModel(articlesRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(userRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        fun getInstance(context: Context): ViewModelFactory {
            val userRepository = Injection.provideUserRepository(context)
            val articlesRepository = Injection.provideArticlesRepository()
            return ViewModelFactory(userRepository, articlesRepository)
        }
    }
}
