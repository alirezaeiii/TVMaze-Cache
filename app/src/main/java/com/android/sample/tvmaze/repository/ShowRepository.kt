package com.android.sample.tvmaze.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.android.sample.tvmaze.database.ShowDao
import com.android.sample.tvmaze.database.asDomainModel
import com.android.sample.tvmaze.domain.Show
import com.android.sample.tvmaze.domain.asDatabaseModel
import com.android.sample.tvmaze.network.TVMazeService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await

class ShowRepository(
    private val showDao: ShowDao,
    private val api: TVMazeService
) {

    /**
     * A list of shows that can be shown on the screen.
     */
    val shows: LiveData<List<Show>> =
        Transformations.map(showDao.getShows()) {
            it.asDomainModel()
        }

    /**
     * Refresh the shows stored in the offline cache.
     */
    suspend fun refreshShows() = withContext(Dispatchers.IO) {
        val news = api.fetchShowList().await()
        showDao.insertAll(*news.asDatabaseModel())
    }
}