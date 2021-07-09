package com.example.tokenlab.activity

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokenlab.R
import com.example.tokenlab.adapter.GenderListAdapter
import com.example.tokenlab.api.Api
import com.example.tokenlab.api.DataService
import com.example.tokenlab.constants.Constants
import com.example.tokenlab.domains.movie.Movie
import com.example.tokenlab.domains.movie.details.belongs.to.collection.BelongsToCollection
import com.example.tokenlab.domains.movie.details.belongs.to.collection.BelongsToCollectionResponse
import com.example.tokenlab.domains.movie.details.details.MovieDetails
import com.example.tokenlab.domains.movie.details.details.MovieDetailsResponse
import com.example.tokenlab.domains.movie.details.production.company.ProductionCompanyResponse
import com.example.tokenlab.domains.movie.details.production.country.ProductionCountryResponse
import com.example.tokenlab.domains.movie.details.spoken.language.SpokenLanguageResponse
import com.example.tokenlab.extensions.createLoadingDialog
import com.example.tokenlab.extensions.downloadImage
import com.example.tokenlab.extensions.showErrorDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailsActivity : AppCompatActivity() {
    private var movieDetailsToolBar: Toolbar? = null
    private var movieDetailsTitleTextView: TextView? = null
    private var movieDetailsVoteAverageTextView: TextView? = null
    private var movieDetailsImageView: ImageView? = null
    private var movieDetailsReleaseDateTextView: TextView? = null
    private var movieDetailsDateTextView: TextView? = null
    private var movieDetailsVoteTextView: TextView? = null
    private var movieDetailsGenresTextView: TextView? = null
    private var movieDetailsRecyclerView: RecyclerView? = null
    private var loadingDialog: Dialog? = null
    private var movieDetailsCountTextView: TextView? = null
    private var movieDetailsVoteCountTextView: TextView? = null
    private var movieDetailsLanguageTextView: TextView? = null
    private var movieDetailsOriginalLanguageTextView: TextView? = null
    private var movieDetailsOriginalTitleMovieTextView: TextView? = null
    private var movieDetailsOriginalTitleTextView: TextView? = null
    private var movieDetailsBelongsToCollectionTextView: TextView? = null
    private var movieDetailsCollectionTextView: TextView? = null
    private var movieDetailsSpokenLanguagesTextView: TextView? = null
    private var movieDetailsSpokenLanguagesRecyclerView: RecyclerView? = null
    private var movieDetailsProductionCountryTextView: TextView? = null
    private var movieDetailsProductionCountryRecyclerView: RecyclerView? = null
    private var movieDetailsProductionCompanyTextView: TextView? = null
    private var movieDetailsProductionCompanyRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)
        findViewsById()
        loadingDialog = this.createLoadingDialog()
        loadingDialog?.show()
        setupToolBar()
        val clickedMovieId = retrieverClickedMovieId()
        getClickedMovieDetailsFromApi(clickedMovieId)
    }

    private fun getClickedMovieDetailsFromApi(clickedMovieId: Int) {
        val dataService: DataService = Api.setupRetrofit().create(DataService::class.java)
        val call: Call<MovieDetailsResponse> = dataService.recoverMovieDetails(clickedMovieId)
        call.enqueue(object : Callback<MovieDetailsResponse> {
            override fun onResponse(
                call: Call<MovieDetailsResponse>,
                response: Response<MovieDetailsResponse>
            ) {
                getClickedMovieDetailsFromApiOnResponse(response)
            }

            override fun onFailure(call: Call<MovieDetailsResponse>, t: Throwable) {
                getClickedMovieDetailsFromApiOnFailure()
            }
        })
    }

    private fun getClickedMovieDetailsFromApiOnFailure() {
        loadingDialog?.dismiss()
        setVisibilityGoneViews()
        this@MovieDetailsActivity.showErrorDialog(getString(R.string.error_connection_fail))
    }

    private fun getClickedMovieDetailsFromApiOnResponse(response: Response<MovieDetailsResponse>) {
        loadingDialog?.dismiss()
        if (response.isSuccessful && response.body() != null) {
            val movieDetailsResponse = response.body()
            val clickedMovieDetails = getClickedMovieDetails(movieDetailsResponse)
            val movieDetailsGenres: List<String> = getMovieDetailsGenres(movieDetailsResponse)
            val productionCompanies: List<ProductionCompanyResponse> =
                getMovieDetailsProductionCompanies(movieDetailsResponse)
            val productionCountries: List<ProductionCountryResponse> =
                movieDetailsResponse?.productionCountries.orEmpty()
            val spokenLanguages: List<SpokenLanguageResponse> =
                movieDetailsResponse?.spokenLanguages.orEmpty()
            val belongsToCollection: String =
                movieDetailsResponse?.belongsToCollection?.name.orEmpty()
//            showClickedMovieDetails(
//                clickedMovieDetails,
//                movieDetailsGenres,
//                productionCompanies,
//                productionCountries,
//                spokenLanguages,
//                belongsToCollection
//            )
            setVisibilityVisibleViews()
        } else {
            setVisibilityGoneViews()
            this@MovieDetailsActivity.showErrorDialog(getString(R.string.occurred_error))
        }
    }

    private fun getMovieDetailsProductionCompanies(movieDetailsResponse: MovieDetailsResponse?) =
        movieDetailsResponse?.productionCompanies.orEmpty()

    private fun getMovieDetailsGenres(movieDetailsResponse: MovieDetailsResponse?) =
        movieDetailsResponse?.genres.orEmpty()

    private fun setVisibilityVisibleViews() {
        movieDetailsImageView?.visibility = View.VISIBLE
        movieDetailsTitleTextView?.visibility = View.VISIBLE
        movieDetailsVoteAverageTextView?.visibility = View.VISIBLE
        movieDetailsReleaseDateTextView?.visibility = View.VISIBLE
        movieDetailsDateTextView?.visibility = View.VISIBLE
        movieDetailsGenresTextView?.visibility = View.VISIBLE
        movieDetailsRecyclerView?.visibility = View.VISIBLE
        movieDetailsVoteTextView?.visibility = View.VISIBLE
        movieDetailsCountTextView?.visibility = View.VISIBLE
        movieDetailsVoteCountTextView?.visibility = View.VISIBLE
        movieDetailsLanguageTextView?.visibility = View.VISIBLE
        movieDetailsOriginalLanguageTextView?.visibility = View.VISIBLE
        movieDetailsOriginalTitleMovieTextView?.visibility = View.VISIBLE
        movieDetailsOriginalTitleTextView?.visibility = View.VISIBLE
        movieDetailsBelongsToCollectionTextView?.visibility = View.VISIBLE
        movieDetailsCollectionTextView?.visibility = View.VISIBLE
        movieDetailsSpokenLanguagesTextView?.visibility = View.VISIBLE
        movieDetailsSpokenLanguagesRecyclerView?.visibility = View.VISIBLE
        movieDetailsProductionCountryTextView?.visibility = View.VISIBLE
        movieDetailsProductionCountryRecyclerView?.visibility = View.VISIBLE
        movieDetailsProductionCompanyTextView?.visibility = View.VISIBLE
        movieDetailsProductionCompanyRecyclerView?.visibility = View.VISIBLE
    }

    private fun setVisibilityGoneViews() {
        movieDetailsImageView?.visibility = View.GONE
        movieDetailsTitleTextView?.visibility = View.GONE
        movieDetailsVoteAverageTextView?.visibility = View.GONE
        movieDetailsReleaseDateTextView?.visibility = View.GONE
        movieDetailsDateTextView?.visibility = View.GONE
        movieDetailsGenresTextView?.visibility = View.GONE
        movieDetailsRecyclerView?.visibility = View.GONE
        movieDetailsVoteTextView?.visibility = View.GONE
        movieDetailsCountTextView?.visibility = View.GONE
        movieDetailsVoteCountTextView?.visibility = View.GONE
        movieDetailsLanguageTextView?.visibility = View.GONE
        movieDetailsOriginalLanguageTextView?.visibility = View.GONE
        movieDetailsOriginalTitleMovieTextView?.visibility = View.GONE
        movieDetailsOriginalTitleTextView?.visibility = View.GONE
        movieDetailsBelongsToCollectionTextView?.visibility = View.GONE
        movieDetailsCollectionTextView?.visibility = View.GONE
        movieDetailsSpokenLanguagesTextView?.visibility = View.GONE
        movieDetailsSpokenLanguagesRecyclerView?.visibility = View.GONE
        movieDetailsProductionCountryTextView?.visibility = View.GONE
        movieDetailsProductionCountryRecyclerView?.visibility = View.GONE
        movieDetailsProductionCompanyTextView?.visibility = View.GONE
        movieDetailsProductionCompanyRecyclerView?.visibility = View.GONE
    }

    private fun showClickedMovieDetails(
        clickedMovie: MovieDetails,
        movieDetailsGenres: List<String>
    ) {
        movieDetailsTitleTextView?.text = clickedMovie.title
        movieDetailsVoteAverageTextView?.text = clickedMovie.voteAverage.toString()
        movieDetailsImageView?.downloadImage(clickedMovie.imageUrl)
        movieDetailsDateTextView?.text = clickedMovie.releaseDate
        val genderListAdapter = GenderListAdapter(movieDetailsGenres)
        setupAdapter(genderListAdapter)
    }

    private fun setupAdapter(genderListAdapter: GenderListAdapter) {
        movieDetailsRecyclerView?.adapter = genderListAdapter
        val layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )
        movieDetailsRecyclerView?.layoutManager = layoutManager
    }

    private fun getClickedMovieDetails(movieDetailsResponse: MovieDetailsResponse?) =
        MovieDetails(
            movieDetailsResponse?.title.orEmpty(),
            movieDetailsResponse?.voteAverage ?: Constants.NULL_DOUBLE_RESPONSE,
            movieDetailsResponse?.voteCount ?: Constants.NULL_INT_RESPONSE,
            movieDetailsResponse?.releaseDate.orEmpty(),
            movieDetailsResponse?.imageUrl.orEmpty(),
            movieDetailsResponse?.originalLanguage.orEmpty(),
            movieDetailsResponse?.originalTitle.orEmpty(),
            movieDetailsResponse?.tagline.orEmpty()
        )

    private fun retrieverClickedMovieId(): Int {
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
        movieDetailsReleaseDateTextView = findViewById(R.id.movieDetailsReleaseDateTextView)
        movieDetailsDateTextView = findViewById(R.id.movieDetailsDateTextView)
        movieDetailsGenresTextView = findViewById(R.id.movieDetailsGenresTextView)
        movieDetailsRecyclerView = findViewById(R.id.movieDetailsRecyclerView)
        movieDetailsVoteTextView = findViewById(R.id.movieDetailsVoteTextView)
        movieDetailsCountTextView = findViewById(R.id.movieDetailsCountTextView)
        movieDetailsVoteCountTextView = findViewById(R.id.movieDetailsVoteCountTextView)
        movieDetailsLanguageTextView = findViewById(R.id.movieDetailsLanguageTextView)
        movieDetailsOriginalLanguageTextView =
            findViewById(R.id.movieDetailsOriginalLanguageTextView)
        movieDetailsOriginalTitleMovieTextView =
            findViewById(R.id.movieDetailsOriginalTitleMovieTextView)
        movieDetailsOriginalTitleTextView = findViewById(R.id.movieDetailsOriginalTitleTextView)
        movieDetailsBelongsToCollectionTextView =
            findViewById(R.id.movieDetailsBelongsToCollectionTextView)
        movieDetailsCollectionTextView = findViewById(R.id.movieDetailsCollectionTextView)
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
    }
}