package com.example.movieproject.ui.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.movieproject.R
import com.example.movieproject.ui.home.MovieClickListener
import com.example.movieproject.utill.FragmentKeys.Companion.IMAGE_PATH
import com.example.movieproject.utill.FragmentKeys.Companion.OVERVIEW
import com.example.movieproject.utill.FragmentKeys.Companion.TITLE
import com.example.movieproject.utill.FragmentKeys.Companion.VOTE_AVERAGE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_details.*

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {


    private val TAG = "DetailsFragment"
    private var mTitle: String? = ""
    private var mOverview: String? = ""
    private var mImagePath: String? = null
    private var mVoteAverage: String? = null

    private var mNavController: NavController? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            getMovie()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mNavController = Navigation.findNavController(view)
        build()
        setMovies()
    }

    private fun build() {
        init()
    }

    private fun init() {
        imageViewBack.setOnClickListener{
            mNavController!!.popBackStack()
        }
    }

    private fun setMovies() {
        textViewVoteAverage.text = mVoteAverage
        textViewTitleDetails.text = mTitle
        editTextOverView.text = mOverview
        val url = "https://image.tmdb.org/t/p/w500$mImagePath"
        Glide.with(this).load(url).into(imageViewPoster2)
    }

    /**
     * gets clicked movie object from previous fragment
     */
    private fun getMovie() {
        val bundle = requireArguments()
        mTitle = bundle.getString(TITLE)
        mOverview = bundle.getString(OVERVIEW)
        mImagePath = bundle.getString(IMAGE_PATH)
        mVoteAverage = bundle.getString(VOTE_AVERAGE)
    }
}