package com.example.csv.logic.usecase

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.example.csv.R
import com.example.csv.logic.data.Cell
import com.example.csv.logic.data.IRepository
import com.example.csv.logic.data.Row
import com.example.parser.api.IParser
import java.io.Reader

open class LoadAndSaveRecordsUseCase(
    private val context: Context,
    private val parser: IParser<List<String>>,
    private val repository: IRepository,
    @VisibleForTesting
    private val getReader: () -> Reader = { context.resources.openRawResource(R.raw.issues).reader() }
) {

    open suspend fun execute(force: Boolean = false) {
        if (!repository.isEmpty() && !force) {
            return
        }

        repository.clearRows()

        var reader: Reader? = null

        try {
            reader = getReader()
            val parsedValues = parser.parse(reader)

            parsedValues.forEachIndexed { index, rowList ->
                val row = rowList
                    .map { data ->
                        Cell.from(data)
                    }
                    .toMutableList()
                    .let { cellList ->
                        Row.from(cellList)
                    }

                if (index == 0) {
                    repository.saveHeaders(row)
                } else {
                    repository.addRow(row)
                }
            }

        } finally {
            reader?.close()
        }
    }

}
