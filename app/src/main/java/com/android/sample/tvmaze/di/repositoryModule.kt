package com.android.sample.tvmaze.di

import com.android.sample.tvmaze.repository.ShowRepository
import org.koin.dsl.module

val repositoryModule = module {

    single { ShowRepository(get(), get(), get()) }
}