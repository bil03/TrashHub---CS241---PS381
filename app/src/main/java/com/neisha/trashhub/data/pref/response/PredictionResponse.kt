package com.neisha.trashhub.data.pref.response

import com.google.gson.annotations.SerializedName

data class PredictionResponse(

	@field:SerializedName("predictionResult")
	val predictionResult: PredictionResult? = null,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class PredictionResult(

	@field:SerializedName("result")
	val result: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("suggestion")
	val suggestion: String,

	@field:SerializedName("imageUrl")
	val imageUrl: String,

	@field:SerializedName("recyclePercentage")
	val recyclePercentage: Int,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("explanation")
	val explanation: String,

	@field:SerializedName("userId")
	val userId: String
)
