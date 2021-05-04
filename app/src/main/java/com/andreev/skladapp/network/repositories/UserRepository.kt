package com.andreev.skladapp.network.repositories

import com.andreev.skladapp.network.FuelNetworkService

class UserRepository : FuelNetworkService() {

    suspend fun login() : String? {
        return get(
            AUTH,
            String::class.java
        )
    }

    companion object {
        const val AUTH = "authTest"
    }
}