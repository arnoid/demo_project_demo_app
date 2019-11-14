package com.example.csv.logic.di

import com.example.csv.logic.data.IRepository
import com.example.csv.logic.data.InMemoryRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(): IRepository {
        return InMemoryRepository()
    }
}