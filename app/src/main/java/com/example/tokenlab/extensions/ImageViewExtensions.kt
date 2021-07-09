package com.example.tokenlab.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.tokenlab.R


fun ImageView.downloadImage(imageUrl: String) {
    Glide.with(context).load(imageUrl).error(R.drawable.img_error).into(this)
}