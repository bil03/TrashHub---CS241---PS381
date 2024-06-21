package com.neisha.trashhub.data.pref.response

import com.google.gson.annotations.SerializedName

data class SearchArticleResponse(

	@field:SerializedName("searchResult")
	val searchResult: List<SearchResultItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class SearchResultItem(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("author")
	val author: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("content")
	val content: String
)
