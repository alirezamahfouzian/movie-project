package com.example.movieproject.ui.search

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.movieproject.R
import com.example.movieproject.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search.*

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search),
    View.OnClickListener {

    private var mParam1: String? = null
    private lateinit var mActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments?.getString(ARG_PARAM1)
        }
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        mActivity = activity as MainActivity
        build()
        showSoftKeyboard(editTextSearch)
    }

    private fun build() {
        cast()
    }

    private fun cast() {
        imageViewBack!!.setOnClickListener { view: View ->
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

            override fun afterTextChanged(editable: Editable) {}
        })
        imageViewClear!!.setOnClickListener { view: View ->
            onClick(
                view
            )
        }
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

    companion object {
        private const val ARG_PARAM1 = "param1"
    }
}