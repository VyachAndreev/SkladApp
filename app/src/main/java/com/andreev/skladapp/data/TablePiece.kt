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
)