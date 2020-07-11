package com.example.in10.ui.listofmovies.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.in10.Model.Constants.POSTER_URL
import com.example.in10.Model.response.Result
import com.example.in10.R
import kotlinx.android.synthetic.main.movie_description.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MovieItemViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    @SuppressLint("SetTextI18n")
    fun bind(movie: Result?, context: Context) {
        itemView.title.text = movie?.title
        itemView.circular_progress_bar.progress = 10 * (((movie?.voteAverage))?.toFloat() ?: 0) as Float
        val percentage = 10 * (((movie?.voteAverage))?.toFloat() ?: 0) as Float

        itemView.percent.text = """${String.format("%.0f", percentage)}%"""
        itemView.circular_progress_bar.backgroundStrokeColor = ContextCompat.getColor(context, R.color.darkGray)

        if (percentage >= 80) {
            itemView.circular_progress_bar.foregroundStrokeColor = ContextCompat.getColor(context, R.color.green)
        } else {
            itemView.circular_progress_bar.foregroundStrokeColor = ContextCompat.getColor(context, R.color.brightYellow)
        }

        val moviePosterURL = POSTER_URL + movie?.posterPath
        Glide.with(itemView.context).load(moviePosterURL).dontAnimate().into(itemView.image)

        itemView.description.text = movie?.overview
        itemView.date_time.text =  movie?.releaseDate?.let { convertDateToRequiredFormat(it) }
    }
    private fun convertDateToRequiredFormat(givenDate: String): String {
        val outputFormat: DateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
        val inputFormat: DateFormat =  SimpleDateFormat("yyyy-MM-dd", Locale.US)

        val date: Date = inputFormat.parse(givenDate)
        return outputFormat.format(date)
    }

}