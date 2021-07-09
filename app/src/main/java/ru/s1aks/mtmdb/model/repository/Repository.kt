package ru.s1aks.mtmdb.model.repository

import ru.s1aks.mtmdb.model.entities.History
import ru.s1aks.mtmdb.model.entities.Movie
import ru.s1aks.mtmdb.model.entities.MoviesList

interface Repository {
    fun getNewMoviesListFromServer(
        callback: retrofit2.Callback<MoviesList>
    )

    fun getMovieDetailsFromServer(
        id: Int,
        callback: retrofit2.Callback<Movie>
    )

    fun saveToHistory(history: History)

    fun getAllHistory(): List<History>
}