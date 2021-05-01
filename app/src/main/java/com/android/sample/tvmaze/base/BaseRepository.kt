package com.android.sample.tvmaze.base

import android.content.Context
import com.android.sample.tvmaze.R
import com.android.sample.tvmaze.util.Resource
import com.android.sample.tvmaze.util.contextProvider.CoroutineContextProvider
import com.android.sample.tvmaze.util.isNetworkAvailable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class BaseRepository<T>(
    private val context: Context,
    contextProvider: CoroutineContextProvider
) {

    protected abstract suspend fun query(): T

    protected abstract suspend fun fetch(): T

    protected abstract suspend fun saveFetchResult(items: T)

    protected open fun isNotEmpty(it: T) = it != null

    val result: Flow<Resource<T>> = flow {
        emit(Resource.loading())
        query().let {
            if (isNotEmpty(it)) {
                // ****** STEP 1: VIEW CACHE ******
                emit(Resource.success(it))
            }
            if (context.isNetworkAvailable()) {
                try {
                    // ****** STEP 2: MAKE NETWORK CALL, SAVE RESULT TO CACHE ******
                    refresh()
                    // ****** STEP 3: VIEW CACHE ******
                    emit(Resource.success(query()))
                } catch (err: Exception) {
                    emit(Resource.error(context.getString(R.string.failed_refresh_msg), it))
                }
            } else {
                emit(Resource.error(context.getString(R.string.failed_network_msg), it))
            }
        }
    }.flowOn(contextProvider.io)

    suspend fun refresh() {
        saveFetchResult(fetch())
    }
}