package ru.s1aks.mtmdb.model.repository

import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.s1aks.mtmdb.BuildConfig
import ru.s1aks.mtmdb.model.entities.Movie
import ru.s1aks.mtmdb.model.entities.MoviesList

const val BASE_URL = "https://api.themoviedb.org/"
const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/original"
const val LOCALE = "ru"
const val NEW_LIST_CATEGORY = "now_playing"
const val TOP_LIST_CATEGORY = "top_rated"
const val SERVER_ERROR = "Ошибка сервера"
const val REQUEST_ERROR = "Ошибка запроса на сервер"
const val CORRUPTED_DATA = "Неполные данные"

class RemoteDataSource {

    private val movieAPI = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .build().create(MovieAPI::class.java)

    private val moviesListAPI = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .build().create(MoviesListAPI::class.java)

    fun getMovieDetails(id: Int, callback: Callback<Movie>) {
        movieAPI.getMovie(id, BuildConfig.TMDB_API_KEY, LOCALE)
            .enqueue(callback)
    }

    fun getNewMoviesList(callback: Callback<MoviesList>) {
        moviesListAPI.getMoviesList(NEW_LIST_CATEGORY, BuildConfig.TMDB_API_KEY, LOCALE)
            .enqueue(callback)
    }
}

