package com.example.tokenlab.api

import com.example.tokenlab.domains.movie.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

abstract class DataService {
    @GET("movies")
    abstract fun recoverMovieList(): Call<MovieResponse>

    @GET("movies/{id}")
    abstract fun recoverMovieDetails(
        @Path("id") id: Int
    ): Call<MovieResponse>
}