package com.andreev.skladapp.network.repositories

import com.andreev.skladapp.network.FuelNetworkService

class ItemsRepository: FuelNetworkService() {

    suspend fun getHints(tag: String): ArrayList<String>{
        return arrayListOf()
    }
}

