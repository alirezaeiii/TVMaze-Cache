package com.android.sample.tvmaze.di

import com.android.sample.tvmaze.util.CoroutineContextProvider
import org.koin.dsl.module

val ContextProviderModule = module {

    single { CoroutineContextProvider() }
}