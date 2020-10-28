package com.android.sample.tvmaze.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.sample.tvmaze.R
import com.android.sample.tvmaze.database.ShowDao
import com.android.sample.tvmaze.database.asDomainModel
import com.android.sample.tvmaze.domain.Show
import com.android.sample.tvmaze.domain.asDatabaseModel
import com.android.sample.tvmaze.network.TVMazeService
import com.android.sample.tvmaze.util.Resource
import com.android.sample.tvmaze.util.contextProvider.CoroutineContextProvider
import com.android.sample.tvmaze.util.isNetworkAvailable
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

class ShowRepository(
    private val dao: ShowDao,
    private val api: TVMazeService,
    private val context: Context,
    private val contextProvider: CoroutineContextProvider
) {

    private val _shows = MutableLiveData<Resource<List<Show>>>()
    val shows: LiveData<Resource<List<Show>>>
        get() = _shows

    @FlowPreview
    suspend fun fetchShows() {
        _shows.postValue(Resource.loading())
        if (context.isNetworkAvailable()) {
            dao.getShows().flatMapConcat { showsFromDb ->
                if (showsFromDb.isEmpty()) {
                    val apiShows = refreshShows()
                    flow {
                        emit(apiShows)
                    }
                } else {
                    flow {
                        emit(showsFromDb.asDomainModel())
                    }
                }
            }.flowOn(contextProvider.io)
                .catch {
                    _shows.postValue(Resource.error(context.getString(R.string.failed_loading_msg)))
                }.collect {
                    _shows.postValue(Resource.success(it))
                }
        } else {
            _shows.postValue(Resource.error(context.getString(R.string.failed_network_msg)))
        }
    }

    suspend fun refreshShows(): List<Show> {
        val apiShows = api.fetchShowList()
        dao.insertAll(*apiShows.asDatabaseModel())
        return apiShows
    }
}