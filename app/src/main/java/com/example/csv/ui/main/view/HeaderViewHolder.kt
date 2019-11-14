package com.example.csv.ui.main.view

import android.view.View
import com.example.csv.logic.data.Cell
import java.text.SimpleDateFormat

class HeaderViewHolder(itemView: View) : RowViewHolder(itemView) {

    override fun prepareDob(cell: Cell): String = cell.data

    companion object {
        const val INDEX_NAME = 0
        const val INDEX_SURNAME = 1
        const val INDEX_ISSUES_COUNT = 2
        const val INDEX_DOB = 3

        const val INPUT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
        const val OUTPUT_DATE_FORMAT = "dd/MM/yyyy"
        val inputDateFormatter = SimpleDateFormat(INPUT_DATE_FORMAT)
        val outputDateFormatter = SimpleDateFormat(OUTPUT_DATE_FORMAT)
    }

}