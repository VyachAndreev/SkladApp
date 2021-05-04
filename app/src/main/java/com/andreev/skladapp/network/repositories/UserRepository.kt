package com.andreev.skladapp.network.repositories

import com.andreev.skladapp.network.FuelNetworkService

class UserRepository : FuelNetworkService() {

    suspend fun signInUser() : Boolean {
        return loginUser()
    }
}