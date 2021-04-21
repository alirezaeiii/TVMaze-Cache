package com.android.sample.tvmaze.base

import android.content.Context
import com.android.sample.tvmaze.R
import com.android.sample.tvmaze.util.Resource
import com.android.sample.tvmaze.util.contextProvider.CoroutineContextProvider
import com.android.sample.tvmaze.util.isNetworkAvailable
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class BaseRepository<T>(
        private val context: Context,
        private val contextProvider: CoroutineContextProvider
) {

    protected abstract suspend fun query(): List<T>

    protected abstract suspend fun fetch(): List<T>

    protected abstract suspend fun saveFetchResult(requestType: List<T>)

    fun sendRequest() = flow {
        emit(Resource.loading())
        query().let {
            if (it.isNotEmpty()) {
                emit(Resource.success(it))
            }
        }
        try {
            if (context.isNetworkAvailable()) {
                refresh()
                emit(Resource.success(query()))
            } else {
                emit(Resource.error(context.getString(R.string.failed_network_msg)))
            }
        } catch (err: Exception) {
            emit(Resource.error(context.getString(R.string.failed_loading_msg)))
        }
    }.flowOn(contextProvider.io)

    suspend fun refresh() {
        saveFetchResult(fetch())
    }
}