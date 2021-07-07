package com.example.tokenlab.activity

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.tokenlab.R
import com.example.tokenlab.api.Api
import com.example.tokenlab.api.DataService
import com.example.tokenlab.constants.Constants
import com.example.tokenlab.domains.movie.MovieResponse
import com.example.tokenlab.extensions.createLoadingDialog
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
    private var movieDetailsGenresTextView: TextView? = null
    private var movieDetailsRecyclerView: RecyclerView? = null
    private var loadingDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)
        findViewsById()
        loadingDialog = this.createLoadingDialog()
        setupToolBar()
        val clickedMovieId = retrieverClickedMovieId()
        getClickedMovieDetailsFromApi(clickedMovieId)
    }

    private fun getClickedMovieDetailsFromApi(clickedMovieId: Int) {
        val dataService: DataService = Api.setupRetrofit().create(DataService::class.java)
        val call: Call<MovieResponse> = dataService.recoverMovieDetails(clickedMovieId)
        call.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                TODO("Not yet implemented")
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

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
    }
}