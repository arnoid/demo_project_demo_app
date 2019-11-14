package com.example.csv.logic.data

data class Row(var cells: List<Cell>) {
    companion object {
        fun from(cells: List<Cell>): Row {
            return Row(cells)
        }
    }
}

