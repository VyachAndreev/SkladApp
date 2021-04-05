package com.andreev.skladapp.network.repositories

import com.andreev.skladapp.data.Position
import com.andreev.skladapp.network.FuelNetworkService

class NullRepository : FuelNetworkService() {

    suspend fun unite(text: String?) : String? {
        return post(
            UNITE + text,
            String::class.java
        )
    }

    data class GetParameters(val getToStockValues: String)

    suspend fun get(text: String) : String? {
        return postWithJson(
            GET,
            String::class.java,
            GetParameters(text),
        )
    }

    suspend fun ship(include: String, exclude: String) : String? {
        return post(
            "departure?request=$include&except=$exclude",
            String::class.java
        )
    }

    companion object {
        const val UNITE = "union?ids="
        const val GET = "union?ids="
    }
}