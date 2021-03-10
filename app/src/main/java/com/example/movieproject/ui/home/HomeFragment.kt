package com.example.movieproject.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.movieproject.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(R.layout.fragment_home) , View.OnClickListener {

    private var navController: NavController? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        build()
    }

    private fun build() {
        init()
    }

    private fun init() {
        textViewSearch.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0) {
            textViewSearch -> {
                navController?.navigate(R.id.action_homeFragment_to_searchFragment)
            }

        }
    }
}