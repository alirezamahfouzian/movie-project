package com.example.movieproject.ui.search

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.CheckResult
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movieproject.R
import com.example.movieproject.network.entity.MovieEntity
import com.example.movieproject.ui.MainActivity
import com.example.movieproject.ui.home.HomeViewModel
import com.example.movieproject.ui.home.MovieClickListener
import com.example.movieproject.ui.home.MoviesRecyclerViewAdapter
import com.example.movieproject.utill.ErrorHandler
import com.example.movieproject.utill.FragmentKeys
import com.example.movieproject.utill.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search),
    View.OnClickListener, MovieClickListener {

    private val TAG = "SearchFragment"

    private lateinit var mActivity: MainActivity

    private lateinit var mAdapter: MoviesRecyclerViewAdapter

    private var mNavController: NavController? = null

    private val mHomeViewModel: HomeViewModel by activityViewModels()


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        mNavController = Navigation.findNavController(view)

        mActivity = activity as MainActivity

        /* this code solves the problem with opening the soft keyboard
        when coming back from detailsFragment and reloading the list */
        if (!mHomeViewModel.openedFromSearch)
            showSoftKeyboard(editTextSearch)

        build()
    }

    private fun build() {
        cast()
        setRecyclerViewMovie()
        setUpSearchAction()
    }

    private fun cast() {
        imageViewBack!!.setOnClickListener { view: View ->
            onClick(
                view
            )
        }
        imageViewClear!!.setOnClickListener { view: View ->
            onClick(
                view
            )
        }
        editTextSearch!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
                if (charSequence.isEmpty()) {
                    imageViewClear!!.visibility = View.GONE
                } else {
                    imageViewClear!!.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(editable: Editable) {
            }
        })
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.imageViewBack -> mActivity.onBackPressed()
            R.id.imageViewClear -> editTextSearch!!.text!!.clear()
        }
    }

    private fun showSoftKeyboard(view: View?) {
        val inputMethodManager =
            mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        view?.requestFocus()
        inputMethodManager.showSoftInput(view, 0)
    }

    /**
     * this fun searches the query if user stopsd typing for 700
     * milliseconds
     */
    private fun setUpSearchAction() {
        editTextSearch.textChanges().debounce(700)
            .onEach {
                if (!it.isNullOrEmpty()) {
                    if (!mHomeViewModel.openedFromSearch) {
                        mHomeViewModel.searchMovieByName(it.toString())
                    }
                }
                mHomeViewModel.openedFromSearch = false
            }
            .launchIn(lifecycleScope)
    }

    /**
     * this fun uses onTextChanged and emits the changed editText value
     * as flow
     */
    @ExperimentalCoroutinesApi
    @CheckResult
    fun EditText.textChanges(): Flow<CharSequence?> {
        return callbackFlow<CharSequence?> {
            val listener = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) = Unit
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) = Unit

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    offer(s)
                }
            }
            addTextChangedListener(listener)
            awaitClose { removeTextChangedListener(listener) }
        }.onStart { emit(text) }
    }

    private fun setRecyclerViewMovie() {
        mAdapter = MoviesRecyclerViewAdapter(mActivity)
        mAdapter.setListener(this)
        recyclerViewSerachMovies!!.layoutManager = GridLayoutManager(mActivity, 3)
        recyclerViewSerachMovies!!.setHasFixedSize(true)
        recyclerViewSerachMovies!!.adapter = mAdapter
        setRecyclerViewData()
    }

    private fun setRecyclerViewData() {
        mHomeViewModel.observeSearchedMovies.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    mActivity.showProgressBar(true)
                }
                is Resource.Success -> {
                    mActivity.showProgressBar(false)
                    mAdapter.setData(it.data!!)
                }
                is Resource.Error -> {
                    mActivity.showProgressBar(false)
                    mActivity.showSnackBar(ErrorHandler.NO_INTERNET)
                }
            }
        })
    }

    override fun onNoteClickListener(movie: MovieEntity, position: Int) {
        val bundle = Bundle()
        bundle.putString(FragmentKeys.TITLE, movie.title)
        bundle.putString(FragmentKeys.OVERVIEW, movie.overview)
        bundle.putString(FragmentKeys.IMAGE_PATH, movie.imagePath)
        bundle.putString(FragmentKeys.VOTE_AVERAGE, movie.voteAverage)
        mHomeViewModel.openedFromSearch = true
        mActivity.showProgressBar(false)
        mNavController!!.navigate(R.id.action_searchFragment_to_detailsFragment, bundle)
    }
}