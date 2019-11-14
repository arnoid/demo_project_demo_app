package com.example.csv

import android.app.Application
import com.example.csv.logic.di.*

class CsvApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        mainComponent = DaggerMainComponent.builder()
            .parserModule(ParserModule())
            .useCaseModule(UseCaseModule(this))
            .repositoryModule(RepositoryModule())
            .build()
    }

    companion object {
        lateinit var mainComponent: MainComponent
    }
}