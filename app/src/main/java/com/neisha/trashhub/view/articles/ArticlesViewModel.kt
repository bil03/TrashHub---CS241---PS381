package com.neisha.trashhub.view.articles

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neisha.trashhub.data.pref.response.ArticleResultsItem
import com.neisha.trashhub.data.pref.response.SearchResultItem
import com.neisha.trashhub.data.repository.ArticlesRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ArticlesViewModel(private val articlesRepository: ArticlesRepository) : ViewModel() {

    private val _articles = MutableLiveData<List<ArticleResultsItem>>()
    val articles: LiveData<List<ArticleResultsItem>> = _articles

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchArticles() {
        viewModelScope.launch {
            try {
                val response = articlesRepository.getArticles()
                if (response != null && !response.error) {
                    val articleResults = response.articleResults ?: emptyList()
                    _articles.postValue(articleResults)
                } else {
                    _articles.postValue(emptyList())
                    _errorMessage.postValue(response?.message ?: "No articles found.")
                    Log.e("ArticlesViewModel", "API Error: ${response?.message ?: "No message"}")
                }
            } catch (e: HttpException) {
                _errorMessage.postValue("HTTP Exception: ${e.message()}")
                Log.e("ArticlesViewModel", "HTTP Exception: ${e.message()}")
            } catch (e: IOException) {
                _errorMessage.postValue("Network Exception: ${e.message}")
                Log.e("ArticlesViewModel", "Network Exception: ${e.message}")
            } catch (e: Exception) {
                _errorMessage.postValue("Exception: ${e.message}")
                Log.e("ArticlesViewModel", "Exception: ${e.message}")
            }
        }
    }

    fun searchArticles(title: String) {
        viewModelScope.launch {
            try {
                val response = articlesRepository.searchArticles(title)
                if (!response.error) {
                    val searchResults = response.searchResult ?: emptyList()
                    val sortedResults = sortSearchResults(searchResults)
                    val articleResults = convertToArticleResults(sortedResults)
                    _articles.postValue(articleResults)
                } else {
                    if (response.message == "Article not found!") {
                        _articles.postValue(emptyList())
                        _errorMessage.postValue("Article not found!")
                    } else {
                        _articles.postValue(emptyList())
                        _errorMessage.postValue(response.message ?: "Error searching articles")
                    }
                    Log.e("ArticlesViewModel", "API Error: ${response.message ?: "No message"}")
                }
            } catch (e: HttpException) {
                handleHttpException(e)
            } catch (e: IOException) {
                handleIOException(e)
            } catch (e: Exception) {
                handleGenericException(e)
            }
        }
    }

    private fun sortSearchResults(searchResults: List<SearchResultItem?>): List<SearchResultItem> {
        // Filter out null items first, then sort
        val nonNullResults = searchResults.filterNotNull()
        return nonNullResults.sortedByDescending { it.date }
    }

    private fun convertToArticleResults(searchResults: List<SearchResultItem?>): List<ArticleResultsItem> {
        return searchResults.filterNotNull().map { searchResult ->
            ArticleResultsItem(
                date = searchResult.date ?: "",
                image = searchResult.image ?: "",
                author = searchResult.author ?: "",
                id = searchResult.id ?: "",
                title = searchResult.title ?: "",
                content = searchResult.content ?: ""
            )
        }
    }

    private fun handleHttpException(e: HttpException) {
        if (e.code() == 404) {
            _errorMessage.postValue("Article not found!")
        } else {
            _errorMessage.postValue("HTTP Exception: ${e.message()}")
        }
        Log.e("ArticlesViewModel", "HTTP Exception: ${e.message()}")
    }

    private fun handleIOException(e: IOException) {
        _errorMessage.postValue("Network Exception: ${e.message}")
        Log.e("ArticlesViewModel", "Network Exception: ${e.message}")
    }

    private fun handleGenericException(e: Exception) {
        _errorMessage.postValue("Exception: ${e.message}")
        Log.e("ArticlesViewModel", "Exception: ${e.message}")
    }
}
