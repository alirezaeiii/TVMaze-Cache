package com.android.sample.tvmaze.base

import android.content.Context
import com.android.sample.tvmaze.R
import com.android.sample.tvmaze.util.Resource
import com.android.sample.tvmaze.util.isNetworkAvailable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

abstract class BaseRepository<T>(
    context: Context,
    dispatcher: CoroutineDispatcher
) {

    protected abstract suspend fun query(): List<T>

    protected abstract suspend fun fetch(): List<T>

    protected abstract suspend fun saveFetchResult(items: List<T>)

    val result: Flow<Resource<List<T>>> = flow {
        emit(Resource.loading())
        val cache = query()
        if (cache.isNotEmpty()) {
            // ****** STEP 1: VIEW CACHE ******
            emit(Resource.success(cache))
            try {
                // ****** STEP 2: MAKE NETWORK CALL, SAVE RESULT TO CACHE ******
                refresh()
                // ****** STEP 3: VIEW CACHE ******
                emit(Resource.success(query()))
            }catch (t: Throwable) {
                Timber.e(t)
            }
        } else {
            if (context.isNetworkAvailable()) {
                try {
                    // ****** STEP 1: MAKE NETWORK CALL, SAVE RESULT TO CACHE ******
                    refresh()
                    // ****** STEP 2: VIEW CACHE ******
                    emit(Resource.success(query()))
                } catch (t: Throwable) {
                    emit(Resource.error(context.getString(R.string.failed_loading_msg)))
                }
            } else {
                emit(Resource.error(context.getString(R.string.failed_network_msg)))
            }
        }
    }.flowOn(dispatcher)

    suspend fun refresh() {
        saveFetchResult(fetch())
    }
}