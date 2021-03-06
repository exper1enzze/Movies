package com.example.movies.repository

import com.example.movies.constants.SortTypes
import com.example.movies.data.DataApi
import com.example.movies.network.NetworkApi
import com.example.movies.repository.model.Movie
import com.example.movies.repository.model.Review
import com.example.movies.repository.model.Trailer
import com.example.movies.utils.datatypes.Result
import io.reactivex.Observable
import javax.inject.Inject

class RepositoryApiImpl @Inject constructor(
    private val networkSource: NetworkApi,
    private val databaseSource: DataApi
): RepositoryApi {


    override fun getMoviesPage(sortTypes: SortTypes, page: Int): Observable<Result<List<Movie>>> {
        return  getMoviesFromNetworkWithCaching(sortTypes, page)
            .onErrorResumeNext(databaseSource.getMoviesBySearchMethod(sortTypes)
                .toObservable())
    }

    private fun getMoviesFromNetworkWithCaching(sortTypes: SortTypes, page: Int): Observable<Result<List<Movie>>> {
        return networkSource.getMovies(sortTypes, page)
            .doOnSuccess {
                it.data?.let { listMovies ->
                    databaseSource.upsertMovies(listMovies, sortTypes)
                }
            }
            .toObservable()
    }

    override fun searchMovie(query: String, year: Int?, adult: Boolean): Observable<List<Movie>> {
        return networkSource.searchMovie(query, year, adult)
            .toObservable()
    }



    override fun getMovieById(movieId: Int): Observable<Result<Movie>> {
        return databaseSource.getMovieById(movieId)
            .switchIfEmpty(networkSource.getMovieByID(movieId))
            .toObservable()
    }

    override fun addMovieToFavourite(movie: Movie) {
        databaseSource.addMovieToFavourite(movie)
    }

    override fun deleteMovieFromFavourite(movieId: Int) {
        databaseSource.deleteMovieFromFavourite(movieId)
    }

    override fun getReviewsById(movieId: Int): Observable<List<Review>> {
        return networkSource.getReviewById(movieId)
            .toObservable()
    }

    override fun getTrailersById(movieId: Int): Observable<List<Trailer>> {
        return networkSource.getTrailerById(movieId)
            .toObservable()
    }

    override fun getFavouriteMovies(): Observable<List<Movie>> {
        return databaseSource.getFavouriteMovies()
    }
}