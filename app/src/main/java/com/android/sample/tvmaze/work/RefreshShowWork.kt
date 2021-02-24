package com.android.sample.tvmaze.work

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.android.sample.tvmaze.repository.ShowRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.KoinComponent
import org.koin.core.inject

class RefreshShowWork(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params), KoinComponent {

    companion object {
        const val WORK_NAME = "RefreshShowWork"
        private const val WORK_DATA_KEY = "KeyShowWork"
    }

    /**
     * A coroutine-friendly method to do your work.
     */
    @ExperimentalCoroutinesApi
    override suspend fun doWork(): Result {
        val repository: ShowRepository by inject()
        return try {
            val apiShows = repository.getShowsFromRemoteDataSource()

            @SuppressLint("RestrictedApi")
            val data = Data.Builder().put(WORK_DATA_KEY, apiShows).build()
            Result.success(data)
        } catch (err: Exception) {
            Result.failure()
        }
    }
}