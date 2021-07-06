package ru.s1aks.mtmdb.model.repository

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.s1aks.mtmdb.model.entities.MoviesList

interface MoviesListAPI {
    @GET("3/movie/{list_category}")
    fun getMoviesList(
        @Path("list_category") listCategory: String,
        @Query("api_key") key: String,
        @Query("language") lang: String,
    ): Call<MoviesList>
}