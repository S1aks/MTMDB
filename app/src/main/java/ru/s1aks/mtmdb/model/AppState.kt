package ru.s1aks.mtmdb.model

import ru.s1aks.mtmdb.model.entities.Movie

sealed class AppState {
    data class Success(val moviesNewData: List<Movie>, val moviesTopData: List<Movie>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}