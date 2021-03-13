package com.andreev.skladapp.network.repositories

import com.andreev.skladapp.network.FuelNetworkService
import com.andreev.skladapp.network.NetworkResponse

class ItemsRepository: FuelNetworkService() {

    suspend fun getHints(tag: String): Tags?{

    }
}

data class Tags(
    val tags: ArrayList<String>?
) : NetworkResponse()

