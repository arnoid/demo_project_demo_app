package com.example.csv.logic.usecase

import com.example.csv.logic.data.IRepository
import com.example.csv.logic.data.Row

open class ReadRowsUseCase(
    private val repository: IRepository
) {

    open fun execute(): List<Row> {
        return repository.readRows()
    }

}
