package com.example.movieproject.repository

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

    private val searchResultsLiveData: MediatorLiveData<Resource<List<MovieEntity>>> =
        MediatorLiveData()

    fun getAllMovies(pageNumber: Int): LiveData<Resource<List<MovieEntity>>> {
        setAllMoviesValue(Resource.Loading(null))
        movieApi.getAllMovies(pageNumber).observeForever {
            when (it) {
                is ApiSuccessResponse -> {
                    setAllMoviesValue(Resource.Success(it.body.results!!))
                }
                is ApiEmptyResponse -> {
                    setAllMoviesValue(Resource.Error("Empty response"))
                }
                is ApiErrorResponse -> {
                    setAllMoviesValue(Resource.Error(it.errorMessage))
                }
            }
        }
        return allMoviesLiveData
    }

    fun searchMovieByName(query: String): LiveData<Resource<List<MovieEntity>>> {
        setSearchResultValue(Resource.Loading(null))
        movieApi.searchMovieByName(query).observeForever {
            when (it) {
                is ApiSuccessResponse -> {
                    setSearchResultValue(Resource.Success(it.body.results!!))
                }
                is ApiEmptyResponse -> {
                    setSearchResultValue(Resource.Error("Empty response"))
                }
                is ApiErrorResponse -> {
                    setSearchResultValue(Resource.Error(it.errorMessage))
                }
            }
        }
        return searchResultsLiveData
    }

    private fun setAllMoviesValue(newValue: Resource<List<MovieEntity>>) {
        if (allMoviesLiveData.value != newValue) {
            allMoviesLiveData.value = newValue
        }
    }

    private fun setSearchResultValue(newValue: Resource<List<MovieEntity>>) {
        if (searchResultsLiveData.value != newValue) {
            searchResultsLiveData.value = newValue
        }
    }

}