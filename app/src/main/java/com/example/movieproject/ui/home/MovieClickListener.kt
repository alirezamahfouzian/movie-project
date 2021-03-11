package com.example.movieproject.ui.home

import com.example.movieproject.network.entity.MovieEntity

interface MovieClickListener {

    fun onNoteClickListener(movie: MovieEntity, position: Int)

}