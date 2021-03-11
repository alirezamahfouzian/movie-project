package com.example.movieproject.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieproject.R
import com.example.movieproject.network.entity.MovieEntity
import com.example.movieproject.ui.MainActivity
import com.example.movieproject.ui.search.SearchFragment
import com.example.movieproject.utill.ActivityHelper
import com.example.movieproject.utill.ConnectionLiveData
import com.example.movieproject.utill.ErrorHandler.Companion.NO_INTERNET
import com.example.movieproject.utill.FragmentKeys.Companion.IMAGE_PATH
import com.example.movieproject.utill.FragmentKeys.Companion.OVERVIEW
import com.example.movieproject.utill.FragmentKeys.Companion.TITLE
import com.example.movieproject.utill.FragmentKeys.Companion.VOTE_AVERAGE
import com.example.movieproject.utill.Resource
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), View.OnClickListener, MovieClickListener {

    private val TAG = "HomeFragment"

    @Inject
    lateinit var mHelper: ActivityHelper.Helper

    @Inject
    lateinit var mConnectionLiveData: ConnectionLiveData

    private var mNavController: NavController? = null

    private val mHomeViewModel: HomeViewModel by viewModels()

    private lateinit var mActivity: MainActivity

    private lateinit var mAdapter: MoviesRecyclerViewAdapter

    private lateinit var mScrollListener: RecyclerView.OnScrollListener


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mActivity = requireActivity() as MainActivity
        mNavController = Navigation.findNavController(view)
        build()
    }

    private fun build() {
        init()
        setUpRefreshLayout()
        fixRefreshLayoutBug()
        setRecyclerViewMovie()
    }

    private fun setUpRefreshLayout() {
        swipeRefresh.setOnRefreshListener {
            if (mConnectionLiveData.isConnected) {
                mHomeViewModel.getAllMovies(1)
            } else {
                swipeRefresh.isRefreshing = false
                mActivity.showSnackBar(NO_INTERNET)
            }
        }
    }

    private fun init() {
        textViewSearch.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0) {
            textViewSearch -> {
                mHelper.transitToFragment(
                    mActivity,
                    SearchFragment(),
                    true
                )
            }
        }
    }

    private fun setRecyclerViewMovie() {
        mAdapter = MoviesRecyclerViewAdapter(mActivity, this)
        recyclerViewMovie!!.layoutManager = GridLayoutManager(mActivity, 3)
        recyclerViewMovie!!.setHasFixedSize(true)
        recyclerViewMovie!!.adapter = mAdapter
        setRecyclerViewData()
    }

    private fun setRecyclerViewData() {
        if (!mHomeViewModel.gotAllNotes) {
            mHomeViewModel.getAllMovies(1)
        }
        mHomeViewModel.observeAllMovies.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    if (!swipeRefresh.isRefreshing)
                        mActivity.showProgressBar(true)
                    Log.d(TAG, "Loading: ")
                }
                is Resource.Success -> {
                    Log.d(TAG, "Success: ${it.data}")
                    mActivity.showProgressBar(false)
                    mHomeViewModel.gotAllNotes = true
                    swipeRefresh.isRefreshing = false
                    mAdapter.setData(it.data!!)
                }
                is Resource.Error -> {
                    mActivity.showProgressBar(false)
                    mHomeViewModel.gotAllNotes = false
                    swipeRefresh.isRefreshing = false
                    Log.d(TAG, "Error: ${it.message}")
                }
            }
        })
    }

    private fun fixRefreshLayoutBug() {
        var isAppBar = false
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            isAppBar = verticalOffset == 0
        })
        mScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(-1) &&
                    newState == RecyclerView.SCROLL_STATE_IDLE &&
                    isAppBar
                ) {
                    swipeRefresh.isEnabled = true
                } else {
                    if (swipeRefresh.isEnabled)
                        swipeRefresh.isEnabled = false
                }
            }
        }
        recyclerViewMovie.addOnScrollListener(mScrollListener)
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