package com.android.sample.tvmaze.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.android.sample.tvmaze.repository.ShowRepository
import com.android.sample.tvmaze.util.MyResult
import org.koin.core.KoinComponent
import org.koin.core.inject

class RefreshShowWork(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params), KoinComponent {

    /**
     * A coroutine-friendly method to do your work.
     */
    override suspend fun doWork(): Result {
        val repository: ShowRepository by inject()
        val result = repository.refreshShows()
        return if (result.status == MyResult.Status.SUCCESS)
            Result.success()
        else Result.failure()
    }
}