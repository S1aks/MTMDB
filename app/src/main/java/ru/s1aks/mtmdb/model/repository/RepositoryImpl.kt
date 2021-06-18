package ru.s1aks.mtmdb.model.repository

import ru.s1aks.mtmdb.model.entities.Movie
import ru.s1aks.mtmdb.model.entities.getMovies

class RepositoryImpl : Repository {
    override fun getWeatherFromServer(): Movie {
        return Movie()
    }

    override fun getWeatherFromLocalStorage() = getMovies()
}