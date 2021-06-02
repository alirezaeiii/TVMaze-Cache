package com.android.sample.tvmaze.viewmodel

import com.android.sample.tvmaze.base.BaseListRepository
import com.android.sample.tvmaze.base.BaseViewModel
import com.android.sample.tvmaze.domain.Show

class MainViewModel(repository: BaseListRepository<Show>) : BaseViewModel<List<Show>>(repository)