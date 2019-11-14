package com.example.csv.logic.di

import com.example.parser.api.Parser
import com.example.parser.csv.CsvParser
import dagger.Module
import dagger.Provides

@Module
open class ParserModule {

    @Provides
    open fun provideParser(): Parser<List<String>> {
        return CsvParser()
    }
}