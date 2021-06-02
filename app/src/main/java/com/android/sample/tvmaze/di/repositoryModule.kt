package com.android.sample.tvmaze.di

import com.android.sample.tvmaze.base.BaseListRepository
import com.android.sample.tvmaze.domain.Show
import com.android.sample.tvmaze.repository.ShowRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<BaseListRepository<Show>> { ShowRepository(get(), get(), get(), get()) }
}