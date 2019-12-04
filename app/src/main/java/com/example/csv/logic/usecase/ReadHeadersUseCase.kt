package com.example.csv.logic.usecase

import com.example.csv.logic.data.IRepository
import com.example.csv.logic.data.Row

open class ReadHeadersUseCase(
    private val repository: IRepository
) {

    open suspend fun execute(): Row {
        return repository.readHeaders()
    }

}
