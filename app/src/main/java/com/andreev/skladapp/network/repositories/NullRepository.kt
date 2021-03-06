package com.andreev.skladapp.network.repositories

import com.andreev.skladapp.data.User
import com.andreev.skladapp.network.FuelNetworkService

class NullRepository : FuelNetworkService() {
    suspend fun unite(text: String?, user: User) : String? {
        return post(
            UNITE + text,
            String::class.java,
            user = user
        )
    }

    private data class GetParameters(val getToStockValues: String)

    suspend fun get(text: String, user: User) : String? {
        return postWithJson(
            GET,
            String::class.java,
            GetParameters(text),
            user = user
        )
    }

    companion object {
        private const val UNITE = "api/union?ids="
        private const val GET = "api/union?ids="
    }
}