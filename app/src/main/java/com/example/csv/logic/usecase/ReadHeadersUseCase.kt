package com.example.csv.logic.usecase

import com.example.csv.logic.data.IRepository
import com.example.csv.logic.data.Row

class ReadHeadersUseCase(
    private val repository: IRepository
) {

    fun execute(): Row {
        return repository.readHeaders()
    }

}
