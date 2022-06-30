package com.android.sample.tvmaze.di

import com.android.sample.tvmaze.base.BaseRepository
import com.android.sample.tvmaze.domain.Show
import com.android.sample.tvmaze.repository.ShowRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {

    single<BaseRepository<Show>> { ShowRepository(get(), get(), get(), get(named("io"))) }
}