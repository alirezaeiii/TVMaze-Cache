package com.android.sample.tvmaze.viewmodel

import com.android.sample.tvmaze.base.BaseRepository

open class BaseListViewModel<T>(repository: BaseRepository<List<T>>)
    : BaseViewModel<List<T>>(repository) 