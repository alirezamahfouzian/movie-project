package com.example.movieproject.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movieproject.R
import com.example.movieproject.network.entity.MovieEntity
import com.example.movieproject.ui.MainActivity
import com.example.movieproject.utill.ConnectionLiveData
import com.example.movieproject.utill.ErrorHandler.Companion.NO_INTERNET
import com.example.movieproject.utill.FragmentKeys.Companion.IMAGE_PATH
import com.example.movieproject.utill.FragmentKeys.Companion.OVERVIEW
import com.example.movieproject.utill.FragmentKeys.Companion.TITLE
import com.example.movieproject.utill.FragmentKeys.Companion.VOTE_AVERAGE
import com.example.movieproject.utill.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), View.OnClickListener, MovieClickListener {

    private val TAG = "HomeFragment"

    @Inject
    lateinit var mConnectionLiveData: ConnectionLiveData

    private var mNavController: NavController? = null

    private val mHomeViewModel: HomeViewModel by viewModels()

    private lateinit var mActivity: MainActivity

    private lateinit var mAdapter: MoviesRecyclerViewAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mActivity = requireActivity() as MainActivity
        mNavController = Navigation.findNavController(view)
        build()
    }

    private fun build() {
        init()
        setRecyclerViewMovie()
    }

    private fun init() {
        textViewSearch.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0) {
            textViewSearch -> {
                mActivity.showProgressBar(false)
                mNavController!!.navigate(R.id.action_homeFragment_to_searchFragment)
            }
        }
    }

    private fun setRecyclerViewMovie() {
        mAdapter = MoviesRecyclerViewAdapter(mActivity)
        mAdapter.setListener(this)
        recyclerViewMovie!!.layoutManager = GridLayoutManager(mActivity, 3)
        recyclerViewMovie!!.setHasFixedSize(true)
        recyclerViewMovie!!.adapter = mAdapter
        setRecyclerViewData()
    }

    private fun setRecyclerViewData() {
        if (!mHomeViewModel.gotAllMovies) {
            mHomeViewModel.getAllMovies(1)
        }
        mHomeViewModel.observeAllMovies.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    mActivity.showProgressBar(true)
                }
                is Resource.Success -> {
                    mActivity.showProgressBar(false)
                    mHomeViewModel.gotAllMovies = true
                    mAdapter.setData(it.data!!)
                }
                is Resource.Error -> {
                    mActivity.showProgressBar(false)
                    mActivity.showSnackBar(NO_INTERNET)
                    mHomeViewModel.gotAllMovies = false
                }
            }
        })
    }

    override fun onNoteClickListener(movie: MovieEntity, position: Int) {
        val bundle = Bundle()
        bundle.putString(TITLE, movie.title)
        bundle.putString(OVERVIEW, movie.overview)
        bundle.putString(IMAGE_PATH, movie.imagePath)
        bundle.putString(VOTE_AVERAGE, movie.voteAverage)
        mNavController!!.navigate(R.id.action_homeFragment_to_detailsFragment, bundle)
    }
}