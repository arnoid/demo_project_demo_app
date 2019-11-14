package com.example.csv.logic.di

import com.example.parser.api.Parser
import com.example.parser.csv.CsvParser
import dagger.Module
import dagger.Provides

@Module
class ParserModule {

    @Provides
    fun provideParser(): Parser<List<String>> {
        return CsvParser()
    }
}