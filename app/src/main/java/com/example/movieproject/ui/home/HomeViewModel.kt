package com.example.movieproject.ui.home

import androidx.lifecycle.*
import com.example.movieproject.network.entity.MovieEntity
import com.example.movieproject.repository.MovieRepository
import com.example.movieproject.utill.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mMovieRepository: MovieRepository,
    handle: SavedStateHandle
) : ViewModel() {
    // is true if the getAllMovies get call at start of homeFragment
    var gotAllMovies = false
    // is true if user opens the detailFragment from searchFragment
    var openedFromSearch = false

    private val allMovies: MediatorLiveData<Resource<List<MovieEntity>>> = MediatorLiveData()
    val observeAllMovies: LiveData<Resource<List<MovieEntity>>> get() = allMovies

    private val searchedMovies: MediatorLiveData<Resource<List<MovieEntity>>> = MediatorLiveData()
    val observeSearchedMovies: LiveData<Resource<List<MovieEntity>>> get() = searchedMovies

    fun getAllMovies(pageNumber: Int) {
        val source = mMovieRepository.getAllMovies(pageNumber)
        allMovies.removeSource(source)
        allMovies.addSource(source, Observer {
            allMovies.value = it
        })
    }
    fun searchMovieByName(query: String) {
        val source = mMovieRepository.searchMovieByName(query)
        searchedMovies.removeSource(source)
        searchedMovies.addSource(source, Observer {
            searchedMovies.value = it
        })
    }

}