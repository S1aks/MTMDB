package ru.s1aks.mtmdb.model.repository

import ru.s1aks.mtmdb.model.MovieLoader
import ru.s1aks.mtmdb.model.entities.Movie

class RepositoryImpl : Repository {
    override fun getMovieFromServer(id: Int): Movie {
        val dto = MovieLoader.loadMovie(id)
        return Movie(
            id = dto?.id ?: 0,
            overview = dto?.overview,
            popularity = dto?.popularity,
            poster_path = dto?.poster_path,
            release_date = dto?.release_date,
            status = dto?.status,
            title = dto?.title ?: "",
            vote_average = dto?.vote_average,
            vote_count = dto?.vote_count,
        )
    }

    override fun getNewMoviesFromServer(): List<Movie> {
        val dto = MovieLoader.loadNewMovies()
        val listMovies = mutableListOf<Movie>()
        dto?.results?.forEach {
            listMovies += Movie(
                id = it.id,
                overview = it.overview,
                popularity = it.popularity,
                poster_path = it.poster_path,
                release_date = it.release_date,
                status = it.status,
                title = it.title,
                vote_average = it.vote_average,
                vote_count = it.vote_count,
            )
        }
        return listMovies
    }

    override fun getTopMoviesFromServer(): List<Movie> {
        val dto = MovieLoader.loadTopMovies()
        val listMovies = mutableListOf<Movie>()
        dto?.results?.forEach {
            listMovies += Movie(
                id = it.id,
                overview = it.overview,
                popularity = it.popularity,
                poster_path = it.poster_path,
                release_date = it.release_date,
                status = it.status,
                title = it.title,
                vote_average = it.vote_average,
                vote_count = it.vote_count,
            )
        }
        return listMovies
    }
}