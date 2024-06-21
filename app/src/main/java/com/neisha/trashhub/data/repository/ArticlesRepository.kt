package com.neisha.trashhub.data.repository

import com.neisha.trashhub.data.pref.response.ArticlesResponse
import com.neisha.trashhub.data.pref.response.DetailArticleResponse
import com.neisha.trashhub.data.pref.response.SearchArticleResponse
import com.neisha.trashhub.data.retrofit.ApiService
import java.io.IOException

class ArticlesRepository(private val apiService: ApiService) {

    suspend fun getArticles(): ArticlesResponse {
        try {
            val response = apiService.getArticles()
            if (!response.error) {
                if (response.articleResults != null) {
                    return response
                } else {
                    throw NullPointerException("articleResults is null in API response")
                }
            } else {
                throw java.lang.Exception("API call unsuccessful: ${response.message}")
            }
        } catch (e: java.lang.Exception) {
            throw java.lang.Exception("Error fetching articles", e)
        }
    }

    suspend fun getArticleDetail(id: String): DetailArticleResponse {
        try {
            val response = apiService.getArticleDetail(id)
            if (!response.error) {
                return response
            } else {
                throw java.lang.Exception("API call unsuccessful: ${response.message}")
            }
        } catch (e: java.lang.Exception) {
            throw java.lang.Exception("Error fetching article detail", e)
        }
    }

    suspend fun searchArticles(title: String): SearchArticleResponse {
        try {
            val response = apiService.searchArticles(title)
            if (!response.error) {
                return response
            } else {
                throw Exception("API call unsuccessful: ${response.message}")
            }
        } catch (e: Exception) {
            throw Exception("Error searching articles", e)
        }
    }
}
