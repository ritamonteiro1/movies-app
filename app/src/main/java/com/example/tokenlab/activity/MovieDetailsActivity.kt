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
import com.example.tokenlab.domains.movie.MovieResponse
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
        val call: Call<MovieResponse> = dataService.recoverMovieDetails(clickedMovieId)
        call.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                getClickedMovieDetailsFromApiOnResponse(response)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                getClickedMovieDetailsFromApiOnFailure()
            }
        })
    }

    private fun getClickedMovieDetailsFromApiOnFailure() {
        loadingDialog?.dismiss()
        setVisibilityGoneViews()
        this@MovieDetailsActivity.showErrorDialog(getString(R.string.error_connection_fail))
    }

    private fun getClickedMovieDetailsFromApiOnResponse(response: Response<MovieResponse>) {
        loadingDialog?.dismiss()
        if (response.isSuccessful && response.body() != null) {
            val movieResponse = response.body()
            val clickedMovie = getClickedMovie(movieResponse)
            val movieGenres: List<String> = movieResponse?.genres.orEmpty()
            showClickedMovieDetails(clickedMovie, movieGenres)
            setVisibilityVisibleViews()
        } else {
            setVisibilityGoneViews()
            this@MovieDetailsActivity.showErrorDialog(getString(R.string.occurred_error))
        }
    }

    private fun setVisibilityVisibleViews() {
        movieDetailsImageView?.visibility = View.VISIBLE
        movieDetailsTitleTextView?.visibility = View.VISIBLE
        movieDetailsVoteAverageTextView?.visibility = View.VISIBLE
        movieDetailsReleaseDateTextView?.visibility = View.VISIBLE
        movieDetailsDateTextView?.visibility = View.VISIBLE
        movieDetailsGenresTextView?.visibility = View.VISIBLE
        movieDetailsRecyclerView?.visibility = View.VISIBLE
        movieDetailsVoteTextView?.visibility = View.VISIBLE
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
    }

    private fun showClickedMovieDetails(clickedMovie: Movie, movieGenres: List<String>) {
        movieDetailsTitleTextView?.text = clickedMovie.title
        movieDetailsVoteAverageTextView?.text = clickedMovie.voteAverage.toString()
        movieDetailsImageView?.downloadImage(clickedMovie.imageUrl)
        movieDetailsDateTextView?.text = clickedMovie.releaseDate
        val genderListAdapter = GenderListAdapter(movieGenres)
        setupAdapter(genderListAdapter)
    }

    private fun setupAdapter(genderListAdapter: GenderListAdapter) {
        movieDetailsRecyclerView?.adapter = genderListAdapter
        val layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )
        movieDetailsRecyclerView?.layoutManager = layoutManager
    }

    private fun getClickedMovie(movieResponse: MovieResponse?) =
        Movie(
            movieResponse?.id ?: Constants.NULL_INT_RESPONSE,
            movieResponse?.voteAverage ?: Constants.NULL_DOUBLE_RESPONSE,
            movieResponse?.title.orEmpty(),
            movieResponse?.imageUrl.orEmpty(),
            movieResponse?.releaseDate.orEmpty()
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
    }
}