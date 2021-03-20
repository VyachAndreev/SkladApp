package com.andreev.skladapp.network.repositories

import com.andreev.skladapp.data.Position
import com.andreev.skladapp.network.FuelNetworkService

class ItemsRepository : FuelNetworkService(){

    suspend fun getHints(tag: String?): Array<String>? {
        return get(TAGS_PATH + tag, Array<String>::class.java)
    }

    suspend fun getPosition(text: String?): Position? {
        return get(
            POSITION_PATH + text,
            Position::class.java
        )
    }

    suspend fun getPositions(text: String?): Array<Position>? {
        return get(
            SEARCH_PATH + text,
            Array<Position>::class.java
        )
    }

    companion object {
        const val TAGS_PATH = "search/tag/"
        const val SEARCH_PATH = "search/"
        const val POSITION_PATH = "position/"
    }
}

