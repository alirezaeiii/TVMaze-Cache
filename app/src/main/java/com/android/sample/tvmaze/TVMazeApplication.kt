@file:Suppress("unused")

package com.android.sample.tvmaze

import android.app.Application
import com.android.sample.tvmaze.di.networkModule
import com.android.sample.tvmaze.di.persistenceModule
import com.android.sample.tvmaze.di.repositoryModule
import com.android.sample.tvmaze.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class TVMazeApplication : Application() {

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
    }
}