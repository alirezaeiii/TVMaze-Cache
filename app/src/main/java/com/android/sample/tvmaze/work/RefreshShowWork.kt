package com.android.sample.tvmaze.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.android.sample.tvmaze.database.ShowDao
import com.android.sample.tvmaze.domain.asDatabaseModel
import com.android.sample.tvmaze.network.TVMazeService
import org.koin.core.KoinComponent
import org.koin.core.inject

class RefreshShowWork(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params), KoinComponent {

    companion object {
        const val WORK_NAME = "RefreshShowWork"
    }

    /**
     * A coroutine-friendly method to do your work.
     */
    override suspend fun doWork(): Result {
        val api: TVMazeService by inject()
        val dao: ShowDao by inject()
        return try {
            val apiShows = api.fetchShowList()
            dao.insertAll(*apiShows.asDatabaseModel())
            Result.success()
        } catch (err: Exception) {
            Result.failure()
        }
    }
}