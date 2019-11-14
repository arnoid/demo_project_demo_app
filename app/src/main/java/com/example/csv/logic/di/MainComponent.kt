package com.example.csv.logic.di

import com.example.csv.ui.main.MainViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ParserModule::class, UseCaseModule::class, RepositoryModule::class])
interface MainComponent {

    fun inject(viewModel: MainViewModel)

}