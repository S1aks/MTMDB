package ru.s1aks.mtmdb.model.repository

import retrofit2.Callback
import ru.s1aks.mtmdb.model.entities.*

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
    fun getCreditsFromServer(id: Int, callback: Callback<Credits>)
    fun getPersonFromServer(id: Int, callback: Callback<Person>)
}