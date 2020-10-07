package com.android.sample.tvmaze.util

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.android.sample.tvmaze.R
import com.android.sample.tvmaze.util.contextProvider.CoroutineContextProvider

fun <T> resultLiveData(
    databaseQuery: () -> LiveData<T>,
    networkCall: suspend () -> Resource<T>, contextProvider: CoroutineContextProvider,
    context: Context
): LiveData<Resource<T>> =
    liveData(contextProvider.io) {
        emit(Resource.loading())
        val source = databaseQuery.invoke().map { Resource.success(it) }
        emitSource(source)

        val result = if (context.isNetworkAvailable()) {
            networkCall.invoke()
        } else {
            Resource.error(context.getString(R.string.failed_network_msg))
        }
        if (result.status == Resource.Status.ERROR) {
            emit(Resource.error(result.message!!))
            emitSource(source)
        }
    }