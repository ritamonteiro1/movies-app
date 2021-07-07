package com.example.tokenlab.domains.movie

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    val id: Int?,
    @SerializedName("voteAverage") val vote_average: Double?,
    val title: String?,
    @SerializedName("imageUrl") val poster_url: String?,
    val genres: List<String>?,
    @SerializedName("releaseDate") val release_date: String?
)