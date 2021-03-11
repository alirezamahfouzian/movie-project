package com.example.movieproject.network.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @Expose
    @SerializedName("page")
    var page: Int?,
    @Expose
    @SerializedName("results")
    var results: List<MovieEntity>?
)
