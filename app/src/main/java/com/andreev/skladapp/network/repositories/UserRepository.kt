package com.andreev.skladapp.network.repositories

import com.andreev.skladapp.network.FuelNetworkService

class UserRepository : FuelNetworkService() {

    private data class User(
        val userName: String,
        val password: String,
    )

    suspend fun signInUser(login: String, password: String) : String? {
        return postWithJson(
            "/login?username=$login&password=$password",
            String::class.java,
            User(login, password),
        )
    }

    companion object {
        const val LOGIN = "login"
    }
}