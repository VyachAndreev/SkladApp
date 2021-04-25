package com.andreev.skladapp.data

data class TablePiece(
    val number: Int,
    val type: String,
    val date: String,
    val place: String,
)

data class HistoryPiece(
    val id: Int,
    val bill: String,
    val contrAgent: String,
    val date: String,
    val mass: String,
    val positions: Array<Position>?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HistoryPiece

        if (id != other.id) return false
        if (bill != other.bill) return false
        if (contrAgent != other.contrAgent) return false
        if (date != other.date) return false
        if (mass != other.mass) return false
        if (positions != null) {
            if (other.positions == null) return false
            if (!positions.contentEquals(other.positions)) return false
        } else if (other.positions != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + bill.hashCode()
        result = 31 * result + contrAgent.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + mass.hashCode()
        result = 31 * result + (positions?.contentHashCode() ?: 0)
        return result
    }
}