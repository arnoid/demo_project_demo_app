package com.example.csv.logic.data

/**
 * Describes data source interactions
 */
interface IRepository {
    fun isEmpty(): Boolean

    fun readRows(): List<Row>
    fun readHeaders(): Row
    fun addRow(row: Row)
    fun clearRows()

    fun saveHeaders(row: Row)
    fun saveRows(newRows: List<Row>)


}