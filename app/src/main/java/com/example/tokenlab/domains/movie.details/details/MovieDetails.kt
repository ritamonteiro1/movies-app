package com.example.tokenlab.domains.movie.details.details

import com.example.tokenlab.domains.movie.details.belongs.to.collection.BelongsToCollection
import com.example.tokenlab.domains.movie.details.production.company.ProductionCompanyResponse
import com.example.tokenlab.domains.movie.details.production.country.ProductionCountryResponse
import com.example.tokenlab.domains.movie.details.spoken.language.SpokenLanguageResponse
import com.google.gson.annotations.SerializedName

data class MovieDetails(
    val title: String,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("poster_url") val imageUrl: String,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("original_title") val originalTitle: String,
    val tagline: String
)
