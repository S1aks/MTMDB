package ru.s1aks.mtmdb.model.repository

import ru.s1aks.mtmdb.model.entities.Movie

interface Repository {
    fun getWeatherFromServer(): Movie
    fun getWeatherFromLocalStorage(): List<Movie>
}