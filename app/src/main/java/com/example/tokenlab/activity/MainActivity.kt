package com.example.tokenlab.activity

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.tokenlab.R

class MainActivity : AppCompatActivity() {
    private var mainToolBar: Toolbar? = null
    private var mainRecyclerView: RecyclerView? = null
    private var loadingDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewsById()
    }

    private fun findViewsById() {
        mainToolBar = findViewById(R.id.mainToolBar)
        mainRecyclerView = findViewById(R.id.mainRecyclerView)
    }
}