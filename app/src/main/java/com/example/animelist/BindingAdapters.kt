package com.example.animelist

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.animelist.ui.AnimeApiStatus

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(imgView)
    }
}

@BindingAdapter("animeApiStatus")
fun bindStatus(statusImageView: ImageView, status: AnimeApiStatus?) {
    when (status) {
        AnimeApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        AnimeApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        AnimeApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}