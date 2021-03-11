package com.example.movieproject.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.movieproject.network.api.MovieApi
import com.example.movieproject.network.entity.MovieEntity
import com.example.movieproject.utill.ApiEmptyResponse
import com.example.movieproject.utill.ApiErrorResponse
import com.example.movieproject.utill.ApiSuccessResponse
import com.example.movieproject.utill.Resource
import javax.inject.Inject

class MovieRepository @Inject constructor(
    val movieApi: MovieApi
) {

    private val TAG = "MovieRepository"

    private val allMoviesLiveData: MediatorLiveData<Resource<List<MovieEntity>>> =
        MediatorLiveData()

    fun getAllMovies(pageNumber: Int): LiveData<Resource<List<MovieEntity>>> {
        setValue(Resource.Loading(null))
        movieApi.getAllMovies(pageNumber).observeForever {
            when (it) {
                is ApiSuccessResponse -> {
                    setValue(Resource.Success(it.body.results!!))
                }
                is ApiEmptyResponse -> {
                    setValue(Resource.Error("Empty response"))
                }
                is ApiErrorResponse -> {
                    setValue(Resource.Error(it.errorMessage))
                }
            }
        }
        return allMoviesLiveData
    }

    private fun setValue(newValue: Resource<List<MovieEntity>>) {
        if (allMoviesLiveData.value != newValue) {
            allMoviesLiveData.value = newValue
        }
    }

}