package com.andreev.skladapp.network.repositories

import com.andreev.skladapp.data.HistoryPiece
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

    suspend fun getMarks(): Array<String>? {
        return get(
            MARKS,
            Array<String>::class.java,
        )
    }

    suspend fun getDiameter(): Array<String>? {
        return get(
            DIAMETER,
            Array<String>::class.java,
        )
    }

    fun getPackings(): Array<String>? {
        return arrayOf(
            "Моток",
            "К300",
            "Д300",
            "К415",
            "Д415",
        )
    }

    suspend fun filter(arrayList: ArrayList<ArrayList<String>>): Array<Position>? {
        return postWithJson(
            FILTER,
            Array<Position>::class.java,
            FilterClass(
                arrayList[0],
                arrayList[2],
                arrayList[1][0].toDouble(),
                arrayList[1][1].toDouble(),
                true,
            )
        )
    }

    suspend fun filterTable(arrayList: ArrayList<ArrayList<String>>): Array<Position>? {
        return postWithJson(
            TABLE,
            Array<Position>::class.java,
            FilterClass(
                arrayList[0],
                arrayList[2],
                arrayList[1][0].toDouble(),
                arrayList[1][1].toDouble(),
                true,
            )
        )
    }

    data class FilterClass(
        val mark: ArrayList<String>,
        val packing: ArrayList<String>,
        val DL: Double,
        val DM: Double,
        val table: Boolean,
    )

    suspend fun getHistory(): Array<HistoryPiece>? {
        return get(
            HISTORY,
            Array<HistoryPiece>::class.java,
        )
    }

    suspend fun getTable(): Array<Position>? {
        return get(
            TABLE,
            Array<Position>::class.java
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
        const val MARKS = "position/marks"
        const val DIAMETER = "position/diameter"
        const val FILTER = "filter"
        const val HISTORY = "history/all"
        const val TABLE = "table"
    }
}

