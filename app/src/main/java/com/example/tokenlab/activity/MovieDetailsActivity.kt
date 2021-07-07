package com.example.tokenlab.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.tokenlab.R

class MovieDetailsActivity : AppCompatActivity() {
    private var movieDetailsToolBar: Toolbar? = null
    private var movieDetailsTitleTextView: TextView? = null
    private var movieDetailsVoteAverageTextView: TextView? = null
    private var movieDetailsImageView: ImageView? = null
    private var movieDetailsReleaseDateTextView: TextView? = null
    private var movieDetailsDateTextView: TextView? = null
    private var movieDetailsGenresTextView: TextView? = null
    private var movieDetailsRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)
        findViewsById()
        setupToolBar()
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