package ru.s1aks.mtmdb.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.s1aks.mtmdb.framework.ui.main.MainViewModel
import ru.s1aks.mtmdb.model.repository.Repository
import ru.s1aks.mtmdb.model.repository.RepositoryImpl

val appModule = module {
    single<Repository> { RepositoryImpl() }

    //View models
    viewModel { MainViewModel(get()) }
}