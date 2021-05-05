package com.andreev.skladapp.network.repositories

import com.andreev.skladapp.data.HistoryPiece
import com.andreev.skladapp.data.Position
import com.andreev.skladapp.data.User
import com.andreev.skladapp.network.FuelNetworkService

class ItemsRepository : FuelNetworkService(){

    suspend fun getHints(tag: String?, user: User): Array<String>? {
        return get(TAGS_PATH + tag, Array<String>::class.java, user = user)
    }

    suspend fun getPosition(text: String?, user: User): Position? {
        return get(
            POSITION_PATH + text,
            Position::class.java,
            user = user
        )
    }

    suspend fun getPackage(text: String?, user: User): Position? {
        return get(
            PACKAGE_PATH + text,
            Position::class.java,
            user = user
        )
    }

    suspend fun getPositions(text: String?, user: User): Array<Position>? {
        return get(
            SEARCH_PATH + text,
            Array<Position>::class.java,
            user = user
        )
    }

    suspend fun getPlavHints(tag: String?, user: User): Array<String>? {
        return post(TAGS_PLAV_PATH + tag, Array<String>::class.java, user = user)
    }


    suspend fun getPlavPositions(text: String?, user: User): Array<Position>? {
        return post(
            SEARCH_PLAV_PATH + text,
            Array<Position>::class.java,
            user = user
        )
    }

    suspend fun getAll(user: User): Array<Position>? {
        return get(
            ALL_POSIOTIONS,
            Array<Position>::class.java,
            user = user
        )
    }

    suspend fun getMarks(user: User): Array<String>? {
        return get(
            MARKS,
            Array<String>::class.java,
            user = user
        )
    }

    suspend fun getDiameter(user: User): Array<String>? {
        return get(
            DIAMETER,
            Array<String>::class.java,
            user = user
        )
    }

    suspend fun getPackings(user: User): Array<String>? {
        return get(
            PACKS,
            Array<String>::class.java,
            user = user
        )
    }

    suspend fun filter(arrayList: ArrayList<ArrayList<String>>, user: User): Array<Position>? {
        return postWithJson(
            FILTER,
            Array<Position>::class.java,
            FilterClass(
                arrayList[0],
                arrayList[2],
                arrayList[1][0].toDouble(),
                arrayList[1][1].toDouble(),
                true,
            ),
            user = user
        )
    }

    suspend fun filterTable(arrayList: ArrayList<ArrayList<String>>, user: User): Array<Position>? {
        return postWithJson(
            TABLE,
            Array<Position>::class.java,
            FilterClass(
                arrayList[0],
                arrayList[2],
                arrayList[1][0].toDouble(),
                arrayList[1][1].toDouble(),
                true,
            ),
            user = user
        )
    }

    data class FilterClass(
        val mark: ArrayList<String>,
        val packing: ArrayList<String>,
        val DL: Double,
        val DM: Double,
        val table: Boolean,
    )

    suspend fun getHistory(user: User): Array<HistoryPiece>? {
        return get(
            HISTORY,
            Array<HistoryPiece>::class.java,
            user = user
        )
    }

    suspend fun getTable(user: User): Array<Position>? {
        return get(
            TABLE,
            Array<Position>::class.java,
            user = user
        )
    }

    companion object {
        const val TAGS_PATH = "api/search/tag/"
        const val SEARCH_PATH = "api/search/"
        const val TAGS_PLAV_PATH = "api/search/plavka/tags?plav="
        const val SEARCH_PLAV_PATH = "api/search/plavka?plav="
        const val POSITION_PATH = "api/position/"
        const val PACKAGE_PATH = "api/package?id="
        const val ALL_POSIOTIONS = "api/positions"
        const val MARKS = "api/position/marks"
        const val DIAMETER = "api/position/diameter"
        const val FILTER = "api/filter"
        const val HISTORY = "api/history/all"
        const val TABLE = "api/table"
        const val PACKS = "api/packings"
    }
}

