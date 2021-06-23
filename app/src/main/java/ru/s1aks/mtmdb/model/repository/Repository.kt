package ru.s1aks.mtmdb.model.repository

import ru.s1aks.mtmdb.model.entities.Movie

interface Repository {
    fun getMovieFromServer(): Movie
    fun getNewMovieFromLocalStorage(): List<Movie>
    fun getTopMovieFromLocalStorage(): List<Movie>
}