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
        val resultFromDb = query()
        val resultIsEmpty = resultFromDb.isEmpty()
        if (!resultIsEmpty) {
            emit(Resource.success(resultFromDb))
        }
        try {
            if (context.isNetworkAvailable()) {
                refresh()
                emit(Resource.success(query()))
            } else if (resultIsEmpty) {
                emit(Resource.error(context.getString(R.string.failed_network_msg)))
            }
        } catch (err: Exception) {
            if (resultIsEmpty) emit(Resource.error(context.getString(R.string.failed_loading_msg)))
        }
    }.flowOn(contextProvider.io)

    suspend fun refresh() {
        saveFetchResult(fetch())
    }
}