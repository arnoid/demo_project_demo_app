package com.example.csv.logic.data

interface IRepository {
    fun isEmpty(): Boolean
    fun clearRows()
    fun readRows(): List<Row>
    fun readHeaders(): Row
    fun saveHeaders(row: Row)
    fun saveRows(newRows: List<Row>)
    fun addRow(row: Row)
}