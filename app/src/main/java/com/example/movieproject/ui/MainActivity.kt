package com.example.movieproject.ui

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.movieproject.R
import com.example.movieproject.ui.home.HomeViewModel
import com.example.movieproject.utill.ActivityHelper
import com.example.movieproject.utill.ConnectionLiveData
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivityLog"

    lateinit var mSnackBar: Snackbar

    private var mSnackBarFont: Typeface? = null


    @Inject
    lateinit var mHelper: ActivityHelper.Helper

    @Inject
    lateinit var mConnectionLiveData: ConnectionLiveData

    private val mHomeViewModel: HomeViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mHelper.setRotationPortrait(this)
        createSnack()
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

    fun showProgressBar(isVisible: Boolean) {
        if (isVisible)
            progressBar.visibility = View.VISIBLE
        else
            progressBar.visibility = View.GONE
    }

    private fun createSnack() {
        mSnackBar =
            Snackbar.make(findViewById(android.R.id.content), "", 4000).apply {
                setBackgroundTint(resources.getColor(R.color.red_text))
                setActionTextColor(resources.getColor(R.color.white))
                setTextColor(resources.getColor(R.color.white))
            }
        val snackView: View = mSnackBar.view
        ViewCompat.setLayoutDirection(snackView, ViewCompat.LAYOUT_DIRECTION_LTR)
        snackView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            .apply {
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                typeface = mSnackBarFont
                setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    resources.getDimension(R.dimen.get_info_snackbar_text_size)
                )
            }
        snackView.findViewById<TextView>(com.google.android.material.R.id.snackbar_action)
            .apply {
                setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    resources.getDimension(R.dimen.get_info_snackbar_text_size)
                )
                typeface = mSnackBarFont
            }
    }

    fun showSnackBar(msg: String) {
        mSnackBar.setText(msg).show()
    }

}