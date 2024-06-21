package com.neisha.trashhub.data.pref.response

import com.google.gson.annotations.SerializedName

data class NewestArticleResponse(

	@field:SerializedName("error")
	val error: Boolean?,

	@field:SerializedName("message")
	val message: String?,

	@field:SerializedName("articleResult")
	val articleResult: List<ArticleResultItem?>? = null
)

data class ArticleResultItem(

	@field:SerializedName("date")
	val date: String?,

	@field:SerializedName("image")
	val image: String?,

	@field:SerializedName("author")
	val author: String?,

	@field:SerializedName("id")
	val id: String?,

	@field:SerializedName("title")
	val title: String?,

	@field:SerializedName("content")
	val content: String?
)
