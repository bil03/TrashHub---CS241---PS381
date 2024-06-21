package com.neisha.trashhub.view.articles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neisha.trashhub.data.pref.response.ArticleResult
import com.neisha.trashhub.data.repository.ArticlesRepository
import kotlinx.coroutines.launch

class ArticleDetailViewModel(private val articlesRepository: ArticlesRepository) : ViewModel() {

    private val _article = MutableLiveData<ArticleResult>()
    val article: LiveData<ArticleResult> = _article

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchArticleDetail(id: String) {
        viewModelScope.launch {
            try {
                val response = articlesRepository.getArticleDetail(id)
                if (response != null && !response.error) {
                    _article.postValue(response.articleResult)
                } else {
                    _errorMessage.postValue(response?.message ?: "No article found.")
                }
            } catch (e: Exception) {
                _errorMessage.postValue(e.message)
            }
        }
    }
}
