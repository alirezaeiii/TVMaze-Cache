package com.android.sample.tvmaze.di

import androidx.room.Room
import com.android.sample.tvmaze.R
import com.android.sample.tvmaze.database.ShowDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val persistenceModule = module {

    single {
        Room
            .databaseBuilder(androidApplication(), ShowDatabase::class.java,
                androidApplication().getString(R.string.database))
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<ShowDatabase>().showDao }
}