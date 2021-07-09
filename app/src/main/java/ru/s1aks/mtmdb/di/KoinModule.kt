package ru.s1aks.mtmdb.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.s1aks.mtmdb.framework.ui.details_fragment.DetailsViewModel
import ru.s1aks.mtmdb.framework.ui.history_fragment.HistoryViewModel
import ru.s1aks.mtmdb.framework.ui.main_fragment.MainViewModel
import ru.s1aks.mtmdb.framework.ui.settings_fragment.SettingsViewModel
import ru.s1aks.mtmdb.model.repository.Repository
import ru.s1aks.mtmdb.model.repository.RepositoryImpl

val appModule = module {
    single<Repository> { RepositoryImpl(get()) }

    viewModel { MainViewModel(get()) }
    viewModel { DetailsViewModel(get()) }
    viewModel { SettingsViewModel() }
    viewModel { HistoryViewModel(get()) }
}