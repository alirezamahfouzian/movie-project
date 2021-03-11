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

    var gotAllNotes = false

    private val allMovies: MediatorLiveData<Resource<List<MovieEntity>>> = MediatorLiveData()
    val observeAllMovies: LiveData<Resource<List<MovieEntity>>> get() = allMovies

    fun getAllMovies(pageNumber: Int) {
        val source = mMovieRepository.getAllMovies(pageNumber)
        allMovies.removeSource(source)
        allMovies.addSource(source, Observer {
            allMovies.value = it
        })
    }

}