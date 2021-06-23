package ru.s1aks.mtmdb.model.repository

import ru.s1aks.mtmdb.model.entities.Movie
import ru.s1aks.mtmdb.model.entities.getNewMovies
import ru.s1aks.mtmdb.model.entities.getTopMovies

class RepositoryImpl : Repository {
    override fun getMovieFromServer() = Movie()
    override fun getNewMovieFromLocalStorage() = getNewMovies()
    override fun getTopMovieFromLocalStorage() = getTopMovies()
}