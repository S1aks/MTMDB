package ru.s1aks.mtmdb.model

import ru.s1aks.mtmdb.model.entities.Movie

sealed class MovieState {
    data class Success(val moviesData: List<Movie>) : MovieState()
    data class Error(val error: Throwable) : MovieState()
    object Loading : MovieState()
}