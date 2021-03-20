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
            Position::class.java,
        )
    }

    suspend fun getPackage(text: String?): Position? {
        return get(
            PACKAGE_PATH + text,
            Position::class.java,
        )
    }

    suspend fun getPositions(text: String?): Array<Position>? {
        return get(
            SEARCH_PATH + text,
            Array<Position>::class.java,
        )
    }

    suspend fun getPlavHints(tag: String?): Array<String>? {
        return get(TAGS_PLAV_PATH + tag, Array<String>::class.java)
    }


    suspend fun getPlavPositions(text: String?): Array<Position>? {
        return get(
            SEARCH_PLAV_PATH + text,
            Array<Position>::class.java,
        )
    }

    suspend fun getAll(): Array<Position>? {
        return get(
            ALL_POSIOTIONS,
            Array<Position>::class.java,
        )
    }

    companion object {
        const val TAGS_PATH = "search/tag/"
        const val SEARCH_PATH = "search/"
        const val TAGS_PLAV_PATH = "api/search/plavka/tags?plav="
        const val SEARCH_PLAV_PATH = "search/plavka?plav="
        const val POSITION_PATH = "position/"
        const val PACKAGE_PATH = "package?id="
        const val ALL_POSIOTIONS = "positions"

    }
}

