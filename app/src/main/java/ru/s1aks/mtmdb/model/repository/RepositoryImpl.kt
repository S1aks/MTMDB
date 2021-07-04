package ru.s1aks.mtmdb.model.repository

import retrofit2.Callback
import ru.s1aks.mtmdb.model.entities.Movie
import ru.s1aks.mtmdb.model.entities.MoviesList

class RepositoryImpl(private val remoteDataSource: RemoteDataSource) : Repository {

    override fun getNewMoviesListFromServer(callback: Callback<MoviesList>) {
        remoteDataSource.getNewMoviesList(callback)
    }

    override fun getMovieDetailsFromServer(
        id: Int,
        callback: Callback<Movie>) {
        remoteDataSource.getMovieDetails(id, callback)
    }
}