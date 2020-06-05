@file:Suppress("unused")

package com.android.sample.tvmaze

import android.app.Application
import androidx.work.*
import com.android.sample.tvmaze.di.networkModule
import com.android.sample.tvmaze.di.persistenceModule
import com.android.sample.tvmaze.di.repositoryModule
import com.android.sample.tvmaze.di.viewModelModule
import com.android.sample.tvmaze.work.RefreshShowWork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import java.util.concurrent.TimeUnit

class TVMazeApplication : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@TVMazeApplication)
            modules(networkModule)
            modules(persistenceModule)
            modules(repositoryModule)
            modules(viewModelModule)
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        applicationScope.launch {
            setupRecurringWork()
        }
    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .build()

        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshShowWork>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            RefreshShowWork.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }
}