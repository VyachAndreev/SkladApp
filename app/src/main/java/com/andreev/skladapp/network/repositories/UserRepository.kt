package com.andreev.skladapp.network.repositories

import com.andreev.skladapp.data.User
import com.andreev.skladapp.data.UserResponse
import com.andreev.skladapp.network.FuelNetworkService
import kotlinx.coroutines.delay

class UserRepository : FuelNetworkService() {

    suspend fun signInUser(login: String, password: String) : UserResponse? {
        delay(1000)
        return UserResponse(
            User("123")
        )
    }
}