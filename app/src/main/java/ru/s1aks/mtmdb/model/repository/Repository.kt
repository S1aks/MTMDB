package ru.s1aks.mtmdb.model.repository

import ru.s1aks.mtmdb.model.entities.Movie

interface Repository {
    fun getMovieFromServer(id: Int): Movie
    fun getNewMoviesFromServer(): List<Movie>
    fun getTopMoviesFromServer(): List<Movie>
}