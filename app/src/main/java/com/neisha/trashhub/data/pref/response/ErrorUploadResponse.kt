package com.neisha.trashhub.data.pref.response

import com.google.gson.annotations.SerializedName

data class ErrorUploadResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
