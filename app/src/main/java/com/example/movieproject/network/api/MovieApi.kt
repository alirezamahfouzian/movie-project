package com.example.movieproject.network.api

import androidx.lifecycle.LiveData
import com.example.movieproject.network.entity.MovieEntity
import com.example.movieproject.network.entity.MovieResponse
import com.example.movieproject.utill.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieApi {

    @GET("discover/movie?sort_by=popularity.desc")
    fun getAllMovies(@Query("page") pageNumber: Int) : LiveData<ApiResponse<MovieResponse>>

    @GET("search/movie?include_adult=false")
    fun searchMovieByName(@Query("query") query: String) : LiveData<ApiResponse<MovieResponse>>

}