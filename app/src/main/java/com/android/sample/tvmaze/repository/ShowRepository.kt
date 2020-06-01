package com.android.sample.tvmaze.repository

import android.content.Context
import androidx.lifecycle.Transformations
import com.android.sample.tvmaze.R
import com.android.sample.tvmaze.database.ShowDao
import com.android.sample.tvmaze.database.asDomainModel
import com.android.sample.tvmaze.domain.Show
import com.android.sample.tvmaze.domain.asDatabaseModel
import com.android.sample.tvmaze.network.TVMazeService
import com.android.sample.tvmaze.util.Result
import com.android.sample.tvmaze.util.isNetworkAvailable
import com.android.sample.tvmaze.util.resultLiveData
import retrofit2.HttpException
import retrofit2.await
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ShowRepository(
    private val dao: ShowDao,
    private val api: TVMazeService,
    private val context: Context
) {

    /**
     * A list of shows that can be shown on the screen.
     */
    val shows = resultLiveData(
        databaseQuery = {
            Transformations.map(dao.getShows()) {
                it.asDomainModel()
            }
        },
        networkCall = { refreshShows() })

    /**
     * Refresh the shows stored in the offline cache.
     */
    private suspend fun refreshShows(): Result<List<Show>> =
        try {
            if (isNetworkAvailable(context)) {
                val shows = api.fetchShowList().await()
                dao.insertAll(*shows.asDatabaseModel())
                Result.success(shows)
            } else {
                Result.error(context.getString(R.string.failed_internet_msg))
            }
        } catch (err: HttpException) {
            Result.error(context.getString(R.string.failed_loading_msg))
        } catch (err: UnknownHostException) {
            Result.error(context.getString(R.string.failed_unknown_host_msg))
        } catch (err: SocketTimeoutException) {
            Result.error(context.getString(R.string.failed_socket_timeout_msg))
        }
}