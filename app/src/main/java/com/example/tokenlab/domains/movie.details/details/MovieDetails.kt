package com.example.tokenlab.domains.movie.details.details

import com.google.gson.annotations.SerializedName

data class MovieDetails(
    val title: String,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("poster_url") val imageUrl: String,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("original_title") val originalTitle: String,
    val tagline: String,
    val belongsToCollection: String
)
