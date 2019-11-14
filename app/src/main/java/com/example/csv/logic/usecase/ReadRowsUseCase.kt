package com.example.csv.logic.usecase

import com.example.csv.logic.data.IRepository
import com.example.csv.logic.data.Row

class ReadRowsUseCase(
    private val repository: IRepository
) {

    fun execute(): List<Row> {
        return repository.readRows()
    }

}
