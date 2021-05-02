package com.android.sample.tvmaze.viewmodel

import com.android.sample.tvmaze.domain.Show
import com.android.sample.tvmaze.repository.ShowRepository

class MainViewModel(repository: ShowRepository) : BaseViewModel<List<Show>>(repository)