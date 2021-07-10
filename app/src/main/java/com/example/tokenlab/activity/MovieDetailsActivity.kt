package com.example.tokenlab.activity

import android.app.Dialog
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokenlab.R
import com.example.tokenlab.adapter.*
import com.example.tokenlab.api.Api
import com.example.tokenlab.api.DataService
import com.example.tokenlab.constants.Constants
import com.example.tokenlab.domains.movie.details.details.MovieDetails
import com.example.tokenlab.domains.movie.details.details.MovieDetailsResponse
import com.example.tokenlab.domains.movie.details.movie.details.list.MovieDetailsList
import com.example.tokenlab.domains.movie.details.production.company.ProductionCompany
import com.example.tokenlab.domains.movie.details.production.country.ProductionCountry
import com.example.tokenlab.domains.movie.details.spoken.language.SpokenLanguage
import com.example.tokenlab.extensions.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MovieDetailsActivity : AppCompatActivity() {
    private var movieDetailsToolBar: Toolbar? = null
    private var movieDetailsTitleTextView: TextView? = null
    private var movieDetailsImageView: ImageView? = null
    private var movieDetailsVoteTextView: TextView? = null
    private var movieDetailsVoteAverageTextView: TextView? = null
    private var movieDetailsRecyclerView: RecyclerView? = null
    private var movieDetailsGenresTextView: TextView? = null
    private var movieDetailsGenderRecyclerView: RecyclerView? = null
    private var movieDetailsSpokenLanguagesTextView: TextView? = null
    private var movieDetailsSpokenLanguagesRecyclerView: RecyclerView? = null
    private var movieDetailsProductionCountryTextView: TextView? = null
    private var movieDetailsProductionCountryRecyclerView: RecyclerView? = null
    private var movieDetailsProductionCompanyTextView: TextView? = null
    private var movieDetailsProductionCompanyRecyclerView: RecyclerView? = null
    private var loadingDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)
        findViewsById()
        loadingDialog = this.createLoadingDialog()
        loadingDialog?.show()
        setupToolBar()
        val movieId = retrieverMovieId()
        getMovieDetailsFromApi(movieId)
    }

    private fun getMovieDetailsFromApi(clickedMovieId: Int) {
        val dataService: DataService = Api.setupRetrofit().create(DataService::class.java)
        val call: Call<MovieDetailsResponse> = dataService.recoverMovieDetails(clickedMovieId)
        call.enqueue(object : Callback<MovieDetailsResponse> {
            override fun onResponse(
                call: Call<MovieDetailsResponse>,
                response: Response<MovieDetailsResponse>
            ) {
                getMovieDetailsFromApiOnResponse(response)
            }

            override fun onFailure(call: Call<MovieDetailsResponse>, t: Throwable) {
                getMovieDetailsFromApiOnFailure()
            }
        })
    }

    private fun getMovieDetailsFromApiOnFailure() {
        loadingDialog?.dismiss()
        setVisibilityGoneViews()
        this@MovieDetailsActivity.showErrorDialogWithAction(
            getString(R.string.error_connection_fail)
        ) { _, _ -> finish() }
    }

    private fun getMovieDetailsFromApiOnResponse(response: Response<MovieDetailsResponse>) {
        loadingDialog?.dismiss()
        if (response.isSuccessful && response.body() != null) {
            val movieDetailsResponse = response.body()
            getMovieDetails(movieDetailsResponse)
        } else {
            setVisibilityGoneViews()
            this@MovieDetailsActivity.showErrorDialogWithAction(
                getString(R.string.occurred_error)
            ) { _, _ -> finish() }
        }
    }

    private fun getMovieDetails(movieDetailsResponse: MovieDetailsResponse?) {
        val movieDetails = mapToMovieDetails(movieDetailsResponse)
        val movieDetailsList = mapToMovieDetailsList(movieDetails)
        val genres = mapToGenres(movieDetailsResponse)
        val productionCompanies = mapToProductionCompanies(movieDetailsResponse)
        val productionCountries = mapToProductionCountries(movieDetailsResponse)
        val spokenLanguages = mapToSpokenLanguages(movieDetailsResponse)
        showMovieDetails(
            movieDetails,
            movieDetailsList,
            genres,
            productionCompanies,
            productionCountries,
            spokenLanguages
        )
        setVisibilityVisibleViews()
    }

    private fun mapToMovieDetailsList(movieDetails: MovieDetails): List<MovieDetailsList> {
        return listOf(
            MovieDetailsList(
                getString(R.string.movie_details_release_date_movie_text),
                movieDetails.releaseDate.convertToValidDateFormat()
            ),
            MovieDetailsList(
                getString(R.string.movie_details_vote_count_text),
                movieDetails.voteCount.toString()
            ),
            MovieDetailsList(
                getString(R.string.movie_details_original_language_text),
                movieDetails.originalLanguage
            ),
            MovieDetailsList(
                getString(R.string.movie_details_original_title_text),
                movieDetails.originalTitle
            ),
            MovieDetailsList(
                getString(R.string.movie_details_belongs_to_collection_text),
                movieDetails.belongsToCollection
            ),
            MovieDetailsList(getString(R.string.movie_details_tagline_text), movieDetails.tagline)
        )
    }

    private fun mapToSpokenLanguages(movieDetailsResponse: MovieDetailsResponse?) =
        movieDetailsResponse?.spokenLanguages?.map {
            SpokenLanguage(it.name ?: Constants.NULL_STRING_RESPONSE)
        } ?: emptyList()

    private fun mapToProductionCountries(movieDetailsResponse: MovieDetailsResponse?) =
        movieDetailsResponse?.productionCountries?.map {
            ProductionCountry(it.name ?: Constants.NULL_STRING_RESPONSE)
        } ?: emptyList()

    private fun mapToProductionCompanies(movieDetailsResponse: MovieDetailsResponse?) =
        movieDetailsResponse?.productionCompanies?.map {
            ProductionCompany(
                it.name ?: Constants.NULL_STRING_RESPONSE,
                it.originCountry ?: Constants.NULL_STRING_RESPONSE,
                it.logoUrl.orEmpty()
            )
        } ?: emptyList()

    private fun mapToGenres(movieDetailsResponse: MovieDetailsResponse?) =
        movieDetailsResponse?.genres.orEmpty()

    private fun setVisibilityVisibleViews() {
        movieDetailsImageView?.visibility = View.VISIBLE
        movieDetailsTitleTextView?.visibility = View.VISIBLE
        movieDetailsVoteAverageTextView?.visibility = View.VISIBLE
        movieDetailsGenresTextView?.visibility = View.VISIBLE
        movieDetailsGenderRecyclerView?.visibility = View.VISIBLE
        movieDetailsVoteTextView?.visibility = View.VISIBLE
        movieDetailsSpokenLanguagesTextView?.visibility = View.VISIBLE
        movieDetailsSpokenLanguagesRecyclerView?.visibility = View.VISIBLE
        movieDetailsProductionCountryTextView?.visibility = View.VISIBLE
        movieDetailsProductionCountryRecyclerView?.visibility = View.VISIBLE
        movieDetailsProductionCompanyTextView?.visibility = View.VISIBLE
        movieDetailsProductionCompanyRecyclerView?.visibility = View.VISIBLE
        movieDetailsRecyclerView?.visibility = View.VISIBLE
    }

    private fun setVisibilityGoneViews() {
        movieDetailsImageView?.visibility = View.GONE
        movieDetailsTitleTextView?.visibility = View.GONE
        movieDetailsVoteAverageTextView?.visibility = View.GONE
        movieDetailsGenresTextView?.visibility = View.GONE
        movieDetailsGenderRecyclerView?.visibility = View.GONE
        movieDetailsVoteTextView?.visibility = View.GONE
        movieDetailsSpokenLanguagesTextView?.visibility = View.GONE
        movieDetailsSpokenLanguagesRecyclerView?.visibility = View.GONE
        movieDetailsProductionCountryTextView?.visibility = View.GONE
        movieDetailsProductionCountryRecyclerView?.visibility = View.GONE
        movieDetailsProductionCompanyTextView?.visibility = View.GONE
        movieDetailsProductionCompanyRecyclerView?.visibility = View.GONE
        movieDetailsRecyclerView?.visibility = View.GONE
    }

    private fun showMovieDetails(
        movieDetails: MovieDetails,
        movieDetailsList: List<MovieDetailsList>,
        movieDetailsGenres: List<String>,
        productionCompanies: List<ProductionCompany>,
        productionCountries: List<ProductionCountry>,
        spokenLanguage: List<SpokenLanguage>
    ) {
        movieDetailsTitleTextView?.text = movieDetails.title
        movieDetailsVoteAverageTextView?.text = movieDetails.voteAverage.toString()
        movieDetailsImageView?.downloadImage(movieDetails.imageUrl)
        val movieDetailsListAdapter = MovieDetailsListAdapter(movieDetailsList)
        setupMovieDetailsListAdapter(movieDetailsListAdapter)
        val genderListAdapter = GenderListAdapter(movieDetailsGenres)
        setupGenderListAdapter(genderListAdapter)
        val spokenLanguageListAdapter = SpokenLanguageListAdapter(spokenLanguage)
        setupSpokenLanguageListAdapter(spokenLanguageListAdapter)
        val productionCompanyListAdapter = ProductionCompanyListAdapter(productionCompanies)
        setupProductionCompanyListAdapter(productionCompanyListAdapter)
        val productionCountryListAdapter = ProductionCountryListAdapter(productionCountries)
        setupProductionCountryListAdapter(productionCountryListAdapter)
    }

    private fun setupMovieDetailsListAdapter(movieDetailsListAdapter: MovieDetailsListAdapter) {
        movieDetailsRecyclerView?.adapter = movieDetailsListAdapter
        val layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )
        movieDetailsRecyclerView?.layoutManager = layoutManager
    }

    private fun setupProductionCountryListAdapter(productionCountryListAdapter: ProductionCountryListAdapter) {
        movieDetailsProductionCountryRecyclerView?.adapter = productionCountryListAdapter
        val layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )
        movieDetailsProductionCountryRecyclerView?.layoutManager = layoutManager
    }

    private fun setupProductionCompanyListAdapter(productionCompanyListAdapter: ProductionCompanyListAdapter) {
        movieDetailsProductionCompanyRecyclerView?.adapter = productionCompanyListAdapter
        val layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )
        movieDetailsProductionCompanyRecyclerView?.layoutManager = layoutManager
    }

    private fun setupSpokenLanguageListAdapter(spokenLanguageListAdapter: SpokenLanguageListAdapter) {
        movieDetailsSpokenLanguagesRecyclerView?.adapter = spokenLanguageListAdapter
        val layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )
        movieDetailsSpokenLanguagesRecyclerView?.layoutManager = layoutManager
    }

    private fun setupGenderListAdapter(genderListAdapter: GenderListAdapter) {
        movieDetailsGenderRecyclerView?.adapter = genderListAdapter
        val layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false
        )
        movieDetailsGenderRecyclerView?.layoutManager = layoutManager
    }

    private fun mapToMovieDetails(movieDetailsResponse: MovieDetailsResponse?) =
        MovieDetails(
            movieDetailsResponse?.title ?: Constants.NULL_STRING_RESPONSE,
            movieDetailsResponse?.voteAverage ?: Constants.NULL_DOUBLE_RESPONSE,
            movieDetailsResponse?.voteCount ?: Constants.NULL_INT_RESPONSE,
            movieDetailsResponse?.releaseDate ?: Constants.NULL_STRING_RESPONSE,
            movieDetailsResponse?.imageUrl.orEmpty(),
            movieDetailsResponse?.originalLanguage ?: Constants.NULL_STRING_RESPONSE,
            movieDetailsResponse?.originalTitle ?: Constants.NULL_STRING_RESPONSE, (
                    if (movieDetailsResponse?.tagline?.isBlank() == true) Constants.NULL_STRING_RESPONSE
                    else movieDetailsResponse?.tagline
                        ?: Constants.NULL_STRING_RESPONSE),
            movieDetailsResponse?.belongsToCollection?.name ?: Constants.NULL_STRING_RESPONSE
        )

    private fun retrieverMovieId(): Int {
        return intent.getSerializableExtra(Constants.ID_MOVIE) as Int
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupToolBar() {
        setSupportActionBar(movieDetailsToolBar)
        supportActionBar?.title = getString(R.string.movie_details_tool_bar_title_text)
    }

    private fun findViewsById() {
        movieDetailsToolBar = findViewById(R.id.movieDetailsToolBar)
        movieDetailsTitleTextView = findViewById(R.id.movieDetailsTitleTextView)
        movieDetailsVoteAverageTextView = findViewById(R.id.movieDetailsVoteAverageTextView)
        movieDetailsImageView = findViewById(R.id.movieDetailsImageView)
        movieDetailsGenresTextView = findViewById(R.id.movieDetailsGenresTextView)
        movieDetailsGenderRecyclerView = findViewById(R.id.movieDetailsGenderRecyclerView)
        movieDetailsVoteTextView = findViewById(R.id.movieDetailsVoteTextView)
        movieDetailsSpokenLanguagesTextView = findViewById(R.id.movieDetailsSpokenLanguagesTextView)
        movieDetailsSpokenLanguagesRecyclerView =
            findViewById(R.id.movieDetailsSpokenLanguagesRecyclerView)
        movieDetailsProductionCountryTextView =
            findViewById(R.id.movieDetailsProductionCountryTextView)
        movieDetailsProductionCountryRecyclerView =
            findViewById(R.id.movieDetailsProductionCountryRecyclerView)
        movieDetailsProductionCompanyTextView =
            findViewById(R.id.movieDetailsProductionCompanyTextView)
        movieDetailsProductionCompanyRecyclerView =
            findViewById(R.id.movieDetailsProductionCompanyRecyclerView)
        movieDetailsRecyclerView = findViewById(R.id.movieDetailsRecyclerView)
    }
}