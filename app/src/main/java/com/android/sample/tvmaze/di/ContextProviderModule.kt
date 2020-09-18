package com.android.sample.tvmaze.di

import com.android.sample.tvmaze.util.contextProvider.CoroutineContextProvider
import org.koin.dsl.module

val ContextProviderModule = module {

    single { CoroutineContextProvider() }
}