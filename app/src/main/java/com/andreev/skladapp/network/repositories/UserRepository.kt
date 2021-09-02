package com.andreev.skladapp.network.repositories

import com.andreev.skladapp.data.User
import com.andreev.skladapp.network.FuelNetworkService

class UserRepository : FuelNetworkService() {
    suspend fun login(user: User) : String? {
        return get(
            AUTH,
            String::class.java,
            user = user
        )
    }

    companion object {
        private const val AUTH = "authTest"
    }
}