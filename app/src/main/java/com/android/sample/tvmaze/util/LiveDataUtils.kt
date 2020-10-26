package com.android.sample.tvmaze.util

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.android.sample.tvmaze.R
import com.android.sample.tvmaze.util.contextProvider.CoroutineContextProvider

fun <T> resultLiveData(
    databaseQuery: () -> LiveData<T>,
    networkCall: suspend () -> Unit, contextProvider: CoroutineContextProvider,
    context: Context
): LiveData<Resource<T>> =
    liveData(contextProvider.io) {
        emit(Resource.loading())
        val source = databaseQuery.invoke().map { Resource.success(it) }
        emitSource(source)

        var result: Resource<T>? = null
        if (context.isNetworkAvailable()) {
            try {
                networkCall.invoke()
            } catch (err: Exception) {
                result = Resource.error(context.getString(R.string.failed_loading_msg))
            }
        } else {
            result = Resource.error(context.getString(R.string.failed_network_msg))
        }
        if (result?.status == Resource.Status.ERROR) {
            emit(Resource.error(result.message!!))
            emitSource(source)
        }
    }