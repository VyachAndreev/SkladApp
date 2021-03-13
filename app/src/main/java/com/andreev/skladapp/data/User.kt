package com.andreev.skladapp.data

import com.andreev.skladapp.network.NetworkResponse

data class User(
    val token: String?
)

data class UserResponse(
    val token: String?
) : NetworkResponse()