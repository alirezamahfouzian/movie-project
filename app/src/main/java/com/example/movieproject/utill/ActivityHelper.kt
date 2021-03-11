package com.example.movieproject.utill

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieproject.R
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ActivityHelper {
    class Helper @Inject constructor() {

        private val mDateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        /**
         * makes app fullscreen
         */
        fun setFullScreen(activity: AppCompatActivity): Helper {
            val decorView = activity.window.decorView
            val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
            decorView.systemUiVisibility = uiOptions
            return this
        }

        /**
         * closes fullscreen mode
         */
        fun clearFullScreen(activity: AppCompatActivity?): Helper {
            return this
        }

        @SuppressLint("SourceLockedOrientationActivity")
        fun setRotationPortrait(activity: AppCompatActivity) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        fun setFullscreen(activity: AppCompatActivity, fullscreen: Boolean) {
            val attrs = activity.window.attributes
            if (fullscreen) {
                attrs.flags = attrs.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
            } else {
                attrs.flags = attrs.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
            }
            activity.window.attributes = attrs
        }

        fun transitToFragment(
            activity: AppCompatActivity,
            destination: Fragment?,
            addToBackStack: Boolean
        ) {
            val fn =
                activity.supportFragmentManager.beginTransaction()
            if (addToBackStack) {
                fn.replace(R.id.container, destination!!)
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit()
                return
            }
            fn.replace(R.id.container, destination!!)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit()
        }

        fun removeFragment(
            activity: AppCompatActivity,
            deletedFragment: Fragment?
        ) {
            val fn =
                activity.supportFragmentManager.beginTransaction()
            fn.remove(deletedFragment!!)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit()
        }

        fun setUpRecyclerView(
            context: Context?, recyclerView: RecyclerView,
            adapter: RecyclerView.Adapter<*>?, isHorizontal: Boolean
        ) {
            if (!isHorizontal) {
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.setHasFixedSize(true)
                recyclerView.adapter = adapter
            } else {
                recyclerView.layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.HORIZONTAL, false
                )
                recyclerView.setHasFixedSize(true)
                recyclerView.adapter = adapter
            }
        }

        fun getNowTime(): String {
            return mDateFormat.format(Calendar.getInstance().time)
        }
    }
}