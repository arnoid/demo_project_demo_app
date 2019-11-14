package com.example.csv.ui.main.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.csv.R
import com.example.csv.logic.data.Cell
import com.example.csv.logic.data.Row
import kotlinx.android.synthetic.main.cell_row.view.*
import java.text.SimpleDateFormat

open class RowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun onBind(row: Row) {
        row.cells.forEachIndexed { index, cell ->
            when (index) {
                INDEX_NAME -> itemView.txt_cell_name.text = prepareName(cell)
                INDEX_SURNAME -> itemView.txt_cell_surname.text = prepareSurname(cell)
                INDEX_ISSUES_COUNT -> itemView.txt_cell_issue_count.text = prepareIssueCount(cell)
                INDEX_DOB -> itemView.txt_cell_dob.text = prepareDob(cell)
            }
        }
    }

    open fun prepareName(cell: Cell) = cell.data
    open fun prepareSurname(cell: Cell) = cell.data
    open fun prepareIssueCount(cell: Cell) = cell.data
    open fun prepareDob(cell: Cell) = convertDateToRenderFormat(cell.data)

    private fun convertDateToRenderFormat(dateString: String): String {
        return try {
            inputDateFormatter.parse(dateString)
                .let { date ->
                    outputDateFormatter.format(date)
                }
        } catch (e: Exception) {
            itemView.resources.getString(R.string.unknown_date)
        }
    }

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