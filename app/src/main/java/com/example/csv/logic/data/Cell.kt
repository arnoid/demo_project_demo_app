package com.example.csv.logic.data

data class Cell(var data: String) {
    companion object {
        fun from(data: String): Cell {
            return Cell(data)
        }
    }
}

