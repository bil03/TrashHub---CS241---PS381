package com.neisha.trashhub.data.pref.response

import com.google.gson.annotations.SerializedName

data class RegisterErrorResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)
