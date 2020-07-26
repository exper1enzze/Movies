package com.example.movies.data

import android.app.Application
import android.util.Log
import com.example.movies.constants.SortTypes
import com.example.movies.data.model.Movie
import com.example.movies.network.rawmodel.MovieResponse
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class DataApiImpl(val application: Application): DataApi {

    private val database = MoviesDatabase.getInstance(application)

    override fun getMoviesBySearchMethod(sortMethod: SortTypes): Maybe<List<Movie>> {
        return database.moviesDao().getMoviesBySearchMethod(sortMethod.ordinal)
    }

    override fun getMovieById(movieId: Int): Observable<Movie> {
        return database.moviesDao().getMovieById(movieId)
    }

    override fun deleteAllMovies() {
        database.moviesDao().deleteAllMovies()
    }

    override fun upsertMovie(movie: Movie, sortMethod: SortTypes) {
        database.moviesDao().upsertMovie(
            movie.id,
            movie.voteCount,
            movie.title,
            movie.originalTitle,
            movie.overview,
            movie.posterPath,
            movie.bigPosterPath,
            movie.backdropPath,
            movie.voteAverage,
            movie.releaseDate,
            movie.isFavourite,
            sortMethod.ordinal
        )
    }

}