package com.android.sample.tvmaze.repository

import android.content.Context
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.asLiveData
import com.android.sample.tvmaze.R
import com.android.sample.tvmaze.database.ShowDao
import com.android.sample.tvmaze.database.asDomainModel
import com.android.sample.tvmaze.domain.Show
import com.android.sample.tvmaze.domain.asDatabaseModel
import com.android.sample.tvmaze.network.TVMazeService
import com.android.sample.tvmaze.util.Resource
import com.android.sample.tvmaze.util.contextProvider.CoroutineContextProvider
import com.android.sample.tvmaze.util.resultLiveData
import retrofit2.await

class ShowRepository(
    private val dao: ShowDao,
    private val api: TVMazeService,
    private val context: Context,
    private val contextProvider: CoroutineContextProvider
) {

    /**
     * A list of shows that can be shown on the screen.
     */
    fun shows() = resultLiveData(
        databaseQuery = {
            map(dao.getShows().asLiveData()) {
                it.asDomainModel()
            }
        },
        networkCall = { refreshShows() }, contextProvider = contextProvider, context = context
    )

    /**
     * Refresh the shows stored in the offline cache.
     */
    suspend fun refreshShows(): Resource<List<Show>> =
        try {
            val shows = api.fetchShowList().await()
            dao.insertAll(*shows.asDatabaseModel())
            Resource.success(shows)
        } catch (err: Exception) {
            Resource.error(context.getString(R.string.failed_loading_msg))
        }
}