package com.andreev.skladapp.data

data class Position(
    val comment: String? = null,
    val createdFrom: Long? = null,
    val date: String? = null,
    val diameter: String? = null,
    val id: Long? = null,
    val manufacturer: String? = null,
    val mark: String? = null,
    val mass: Double? = null,
    val packing: String? = null,
    val part: String? = null,
    val plav: String? = null,
    val status: String? = null,
    val type: String? = null,
    val positionsList: Array<Position>? = null,
    val location: String? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Position

        if (comment != other.comment) return false
        if (createdFrom != other.createdFrom) return false
        if (date != other.date) return false
        if (diameter != other.diameter) return false
        if (id != other.id) return false
        if (manufacturer != other.manufacturer) return false
        if (mark != other.mark) return false
        if (mass != other.mass) return false
        if (packing != other.packing) return false
        if (part != other.part) return false
        if (plav != other.plav) return false
        if (status != other.status) return false
        if (type != other.type) return false
        if (positionsList != null) {
            if (other.positionsList == null) return false
            if (!positionsList.contentEquals(other.positionsList)) return false
        } else if (other.positionsList != null) return false
        if (location != other.location) return false

        return true
    }

    override fun hashCode(): Int {
        var result = comment?.hashCode() ?: 0
        result = 31 * result + (createdFrom?.hashCode() ?: 0)
        result = 31 * result + (date?.hashCode() ?: 0)
        result = 31 * result + (diameter?.hashCode() ?: 0)
        result = 31 * result + (id?.hashCode() ?: 0)
        result = 31 * result + (manufacturer?.hashCode() ?: 0)
        result = 31 * result + (mark?.hashCode() ?: 0)
        result = 31 * result + (mass?.hashCode() ?: 0)
        result = 31 * result + (packing?.hashCode() ?: 0)
        result = 31 * result + (part?.hashCode() ?: 0)
        result = 31 * result + (plav?.hashCode() ?: 0)
        result = 31 * result + (status?.hashCode() ?: 0)
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + (positionsList?.contentHashCode() ?: 0)
        result = 31 * result + (location?.hashCode() ?: 0)
        return result
    }
}