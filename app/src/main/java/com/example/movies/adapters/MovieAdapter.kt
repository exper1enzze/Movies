package com.example.movies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.data.Movie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_item.view.*

class MovieAdapter: RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    var onReachEndListener: OnReachEndListener? = null
    var onPosterClickListener: OnPosterClickListener? = null
    var movies = listOf<Movie>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        if ((position > movies.size - 4) && (onReachEndListener != null)) {
            onReachEndListener!!.onReachEnd()
        }
        val movie = movies[position]
        Picasso.get().load(movie.bigPosterPath).into(holder.imageViewSmallPoster)
    }

    override fun getItemCount(): Int {
        return movies.size
    }


    inner class MovieViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        init {
            super.itemView.setOnClickListener {
                onPosterClickListener?.onPosterClick(adapterPosition)
            }
        }

        val imageViewSmallPoster = itemView.imageViewSmallPoster
    }

    interface OnPosterClickListener {
        fun onPosterClick(position: Int)
    }

    interface OnReachEndListener {
        fun onReachEnd();
    }

}