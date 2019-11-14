package com.example.csv.ui.main.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.csv.R
import com.example.csv.logic.data.Row

class RowsAdapter : RecyclerView.Adapter<RowViewHolder>() {

    var headers: Row = Row.from(emptyList())
    var rows: List<Row> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder {
        val layoutRes = when (viewType) {
            VIEW_TYPE_HEADER -> R.layout.cell_header
            VIEW_TYPE_NORMAL -> R.layout.cell_row
            VIEW_TYPE_NORMAL_ALTERNATE -> R.layout.cell_row_alternate
            else -> R.layout.cell_row
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)

        return when (viewType) {
            VIEW_TYPE_HEADER -> HeaderViewHolder(view)
            else -> RowViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == POSITION_HEADER) {
            VIEW_TYPE_HEADER
        } else if (position % 2 == 0) {
            VIEW_TYPE_NORMAL_ALTERNATE
        } else {
            VIEW_TYPE_NORMAL
        }
    }

    override fun getItemCount(): Int {
        val rowsCount = rows.size
        val headersCount = 1// this is a const
        return rowsCount + headersCount
    }

    override fun onBindViewHolder(holder: RowViewHolder, position: Int) {
        if (position == POSITION_HEADER) {
            holder.onBind(headers)
        } else {
            holder.onBind(rows[position - 1])
        }
    }

    companion object {
        const val POSITION_HEADER = 0

        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_NORMAL = 1
        const val VIEW_TYPE_NORMAL_ALTERNATE = 2
    }

}