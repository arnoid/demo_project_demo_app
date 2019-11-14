package com.example.csv.ui.main.view

import android.view.View
import com.example.csv.logic.data.Cell

class HeaderViewHolder(itemView: View) : RowViewHolder(itemView) {

    //Just bypass any parsing
    override fun prepareDob(cell: Cell): String = cell.data

}