package com.example.movies.data

import com.example.movies.constants.SortTypes
import com.example.movies.repository.model.Movie
import com.example.movies.utils.datatypes.Result
import io.reactivex.Maybe
import io.reactivex.Observable

interface DataApi {
    fun getMoviesBySearchMethod(sortMethod: SortTypes): Maybe<Result<List<Movie>>>

    fun getMovieById(movieId: Int): Maybe<Result<Movie>>

    fun deleteAllMovies()

    fun upsertMovies(moviesList: List<Movie>, sortMethod: SortTypes)

    fun addMovieToFavourite(movie: Movie)

    fun deleteMovieFromFavourite(movieId: Int)

    fun getFavouriteMovies(): Observable<List<Movie>>
}