package com.example.movies.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey
    val id: Int,
    val voteCount: Int? = null,
    val title: String = "",
    val originalTitle: String = "",
    val overview: String = "",
    val posterPath: String = "",
    val bigPosterPath: String = "",
    val backdropPath: String = "",
    val voteAverage: Double? = null,
    val releaseDate: String = ""
)