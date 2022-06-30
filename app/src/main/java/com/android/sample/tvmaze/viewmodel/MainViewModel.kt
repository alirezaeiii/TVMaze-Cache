package com.android.sample.tvmaze.viewmodel

import com.android.sample.tvmaze.base.BaseRepository
import com.android.sample.tvmaze.base.BaseViewModel
import com.android.sample.tvmaze.domain.Show

class MainViewModel(repository: BaseRepository<Show>) : BaseViewModel<Show>(repository)