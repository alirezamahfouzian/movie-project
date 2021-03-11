package com.example.movieproject.ui.home

import com.example.movieproject.network.entity.MovieEntity

interface NoteClickListener {

    fun onNoteClickListener(movie: MovieEntity, position: Int)

}