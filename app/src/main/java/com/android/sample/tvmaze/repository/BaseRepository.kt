package com.android.sample.tvmaze.repository

import android.content.Context
import com.android.sample.tvmaze.R
import com.android.sample.tvmaze.util.Resource
import com.android.sample.tvmaze.util.contextProvider.CoroutineContextProvider
import com.android.sample.tvmaze.util.isNetworkAvailable
import kotlinx.coroutines.flow.*
import timber.log.Timber

abstract class BaseRepository<T>(private val context: Context,
                                 private val contextProvider: CoroutineContextProvider
) {

    protected abstract suspend fun query(): Pair<Boolean, T>

    protected abstract suspend fun fetch(): T

    protected abstract suspend fun saveFetchResult(requestType : T)

    suspend fun sendRequest() = flow {
        emit(Resource.loading())
        val queryResult = query()
        if (queryResult.first) {
            if (context.isNetworkAvailable()) {
                saveFetchResult(fetch())
                emit(Resource.success(query().second))
            } else {
                emit(Resource.error(context.getString(R.string.failed_network_msg)))
            }
        } else {
            emit(Resource.success(queryResult.second))
            try {
                saveFetchResult(fetch())
                emit(Resource.success(query().second))
            } catch (err: Exception) {
                Timber.e(err)
            }
        }
    }.flowOn(contextProvider.io).catch {
        emit(Resource.error(context.getString(R.string.failed_loading_msg)))
    }

    suspend fun refresh() {
        val apiShows = fetch()
        saveFetchResult(apiShows)
    }
}