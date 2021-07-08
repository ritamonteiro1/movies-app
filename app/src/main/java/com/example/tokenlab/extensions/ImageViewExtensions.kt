package com.example.tokenlab.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide


fun ImageView.downloadImage(baseImageUrl: String) {
        Glide.with(context).load(baseImageUrl).into(this)
}