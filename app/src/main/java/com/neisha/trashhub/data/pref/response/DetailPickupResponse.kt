package com.neisha.trashhub.view.response

import com.google.gson.annotations.SerializedName

data class DetailPickupResponse(

	@field:SerializedName("pickupResult")
	val pickupResult: PickupResult? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class PickupResult(

	@field:SerializedName("photo")
	val photo: String? = null,

	@field:SerializedName("weight")
	val weight: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("lon")
	val lon: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("mitraId")
	val mitraId: String? = null,

	@field:SerializedName("pickup_date")
	val pickupDate: String? = null,

	@field:SerializedName("notifUser")
	val notifUser: String? = null,

	@field:SerializedName("notifMitra")
	val notifMitra: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("pickup_time")
	val pickupTime: String? = null,

	@field:SerializedName("lat")
	val lat: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
