package com.neisha.trashhub.data.pref.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
	@field:SerializedName("error")
	val error: Boolean,
	@field:SerializedName("message")
	val message: String,
	@field:SerializedName("registerResult")
	val registerResult: RegisterResult?
)

data class RegisterResult(
	@field:SerializedName("phone")
	val phone: String,
	@field:SerializedName("roles")
	val roles: String,
	@field:SerializedName("name")
	val name: String,
	@field:SerializedName("id")
	val id: String,
	@field:SerializedName("email")
	val email: String
)
