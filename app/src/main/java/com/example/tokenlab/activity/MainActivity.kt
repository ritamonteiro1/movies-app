package com.example.tokenlab.activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokenlab.R
import com.example.tokenlab.adapter.MovieListAdapter
import com.example.tokenlab.api.Api
import com.example.tokenlab.api.DataService
import com.example.tokenlab.click.listener.OnMovieButtonClickListener
import com.example.tokenlab.constants.Constants
import com.example.tokenlab.domains.movie.Movie
import com.example.tokenlab.domains.movie.MovieResponse
import com.example.tokenlab.extensions.createLoadingDialog
import com.example.tokenlab.extensions.showErrorDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var mainToolBar: Toolbar? = null
    private var mainRecyclerView: RecyclerView? = null
    private var loadingDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewsById()
        loadingDialog = this.createLoadingDialog()
        setupToolBar()
        loadingDialog?.show()
        getMovieListFromApi()
    }

    private fun getMovieListFromApi() {
        val dataService: DataService = Api.setupRetrofit().create(DataService::class.java)
        val call: Call<List<MovieResponse>> = dataService.recoverMovieList()
        call.enqueue(object : Callback<List<MovieResponse>> {
            override fun onResponse(
                call: Call<List<MovieResponse>>,
                response: Response<List<MovieResponse>>
            ) {
                getMovieListFromApiOnResponse(response)
            }

            override fun onFailure(call: Call<List<MovieResponse>>, t: Throwable) {
                getMovieListFromApiOnFailure()
            }
        })
    }

    private fun getMovieListFromApiOnFailure() {
        loadingDialog?.dismiss()
        mainRecyclerView?.visibility = View.GONE
        this@MainActivity.showErrorDialog(getString(R.string.error_connection_fail))
    }

    private fun getMovieListFromApiOnResponse(response: Response<List<MovieResponse>>) {
        loadingDialog?.dismiss()
        if (response.isSuccessful && response.body() != null) {
            mainRecyclerView?.visibility = View.VISIBLE
            val movieListResponse = response.body()
            val movieList = mapToMovieList(movieListResponse)
            treatMovieListEmpty(movieList)
            val movieListAdapter = createMovieListAdapter(movieList)
            setupAdapter(movieListAdapter)
        } else {
            mainRecyclerView?.visibility = View.GONE
            this@MainActivity.showErrorDialog(getString(R.string.occurred_error))
        }
    }

    private fun createMovieListAdapter(movieList: List<Movie>) =
        MovieListAdapter(movieList, onMovieButtonClickListener())

    private fun onMovieButtonClickListener() = object : OnMovieButtonClickListener {
        override fun onClick(movieId: Int) {
            val intent =
                Intent(this@MainActivity, MovieDetailsActivity::class.java)
            intent.putExtra(Constants.ID_MOVIE, movieId)
            startActivity(intent)
        }
    }

    private fun setupAdapter(movieListAdapter: MovieListAdapter) {
        mainRecyclerView?.adapter = movieListAdapter
        val layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )
        mainRecyclerView?.layoutManager = layoutManager

    }

    private fun treatMovieListEmpty(movieList: List<Movie>) {
        if (movieList.isEmpty()) {
            this.showErrorDialog(getString(R.string.occurred_error))
        }
    }

    private fun mapToMovieList(movieListResponse: List<MovieResponse>?) =
        movieListResponse?.map {
            Movie(
                it.id ?: Constants.NULL_INT_RESPONSE,
                it.voteAverage ?: Constants.NULL_DOUBLE_RESPONSE,
                it.title.orEmpty(),
                it.imageUrl.orEmpty(),
                it.releaseDate.orEmpty()
            )
        } ?: emptyList()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupToolBar() {
        setSupportActionBar(mainToolBar)
        supportActionBar?.title = getString(R.string.main_tool_bar_title_text)
    }

    private fun findViewsById() {
        mainToolBar = findViewById(R.id.mainToolBar)
        mainRecyclerView = findViewById(R.id.mainRecyclerView)
    }
}