package com.example.movieproject.utill

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectionLiveData @Inject constructor(private val context: Application) :
    LiveData<ConnectionModel?>() {

    init {
        networkMonitor()
    }

    var isConnected = false

    @SuppressLint("CheckResult")
    private fun networkMonitor() {
        ReactiveNetwork
            .observeNetworkConnectivity(context)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                isConnected = it.available()
                value = ConnectionModel(isConnected)
                Log.d("testtest", "networkMonitor: $isConnected")
            }
    }

}
