package com.neisha.trashhub.view.response

import com.google.gson.annotations.SerializedName

data class RejectPickupResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
