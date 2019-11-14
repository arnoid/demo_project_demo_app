package com.example.csv.logic.di

import com.example.csv.logic.data.IRepository
import com.example.csv.logic.data.InMemoryRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class RepositoryModule {

    @Singleton
    @Provides
    open fun provideRepository(): IRepository {
        return InMemoryRepository()
    }
}