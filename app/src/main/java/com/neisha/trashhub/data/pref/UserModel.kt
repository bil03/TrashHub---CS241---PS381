package com.neisha.trashhub.data.pref

data class UserModel(
    val id: String,
    val name: String,
    val email: String,
    val token: String,
    val phone: String = "",
    val roles: String = "",
    val isLogin: Boolean
)
