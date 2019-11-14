package com.example.csv.logic.data

data class Row(var cells: MutableList<Cell>) {
    fun clear() {
        cells.clear()
    }

    fun addAll(row: Row) {
        cells.addAll(row.cells)
    }

    companion object {
        fun from(cells: MutableList<Cell>): Row {
            return Row(cells)
        }
    }
}

