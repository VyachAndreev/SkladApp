package com.andreev.skladapp.network.repositories

import com.andreev.skladapp.network.FuelNetworkService

class ItemsRepository : FuelNetworkService(){

    suspend fun getHints(tag: String?): ArrayList<String>? {
        return get(TAGS_PATH + tag, (ArrayList<String>())::class.java)
    }

    companion object {
        const val TAGS_PATH = "search/tag/"
    }
}

