package com.example.csv.logic.di

import android.content.Context
import com.example.csv.logic.data.IRepository
import com.example.csv.logic.usecase.LoadAndSaveRecordsUseCase
import com.example.csv.logic.usecase.ReadHeadersUseCase
import com.example.csv.logic.usecase.ReadRowsUseCase
import com.example.parser.api.IParser
import dagger.Module
import dagger.Provides

@Module
open class UseCaseModule(
    private val context: Context
) {

    @Provides
    open fun provideLoadAndSaveRecordsUseCase(
        parser: IParser<List<String>>,
        repository: IRepository
    ): LoadAndSaveRecordsUseCase {
        return LoadAndSaveRecordsUseCase(context, parser, repository)
    }

    @Provides
    open fun provideReadHeadersUseCase(repository: IRepository): ReadHeadersUseCase {
        return ReadHeadersUseCase(repository)
    }

    @Provides
    open fun provideReadRowsUseCase(repository: IRepository): ReadRowsUseCase {
        return ReadRowsUseCase(repository)
    }
}