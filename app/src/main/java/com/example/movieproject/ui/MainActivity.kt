package com.example.movieproject.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.movieproject.R

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivityLog"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        hideKeyboard(this)
    }

    private fun hideKeyboard(context: Context) {
        try {
            (context as Activity).window
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            if (context.currentFocus != null && context.currentFocus!!
                    .windowToken != null
            ) {
                (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    context.currentFocus!!.windowToken,
                    0
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}