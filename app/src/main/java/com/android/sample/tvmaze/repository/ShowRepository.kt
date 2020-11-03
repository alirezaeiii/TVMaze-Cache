package com.android.sample.tvmaze.repository

import android.content.Context
import com.android.sample.tvmaze.R
import com.android.sample.tvmaze.database.ShowDao
import com.android.sample.tvmaze.database.asDomainModel
import com.android.sample.tvmaze.domain.Show
import com.android.sample.tvmaze.domain.asDatabaseModel
import com.android.sample.tvmaze.network.TVMazeService
import com.android.sample.tvmaze.util.Resource
import com.android.sample.tvmaze.util.contextProvider.CoroutineContextProvider
import com.android.sample.tvmaze.util.isNetworkAvailable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import timber.log.Timber

@ExperimentalCoroutinesApi
class ShowRepository(
    private val dao: ShowDao,
    private val api: TVMazeService,
    private val context: Context,
    private val contextProvider: CoroutineContextProvider
) {

    private val _shows = MutableStateFlow<Resource<List<Show>>>(Resource.idle())
    val shows: StateFlow<Resource<List<Show>>>
        get() = _shows

    @FlowPreview
    suspend fun fetchShows() {
        _shows.value = Resource.loading()
        dao.getShows().let { showsFromDb ->
            flow {
                if (showsFromDb.isEmpty()) {
                    if (context.isNetworkAvailable()) {
                        val apiShows = getRefreshedShows()
                        emit(Resource.success(apiShows))
                    } else {
                        emit(Resource.error(context.getString(R.string.failed_network_msg)))
                    }
                } else {
                    emit(Resource.success(showsFromDb.asDomainModel()))
                    try {
                        val apiShows = getRefreshedShows()
                        emit(Resource.update(apiShows))
                    } catch (err: Exception) {
                        Timber.e(err)
                    }
                }
            }
        }.flowOn(contextProvider.io)
            .catch {
                _shows.value = Resource.error(context.getString(R.string.failed_loading_msg))
            }.collect {
                _shows.value = it
            }
    }

    suspend fun getRefreshedShows(): List<Show> {
        val apiShows = api.fetchShowList()
        dao.insertAll(*apiShows.asDatabaseModel())
        return apiShows
    }
}