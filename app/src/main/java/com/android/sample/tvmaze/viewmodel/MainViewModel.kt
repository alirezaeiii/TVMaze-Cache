package com.android.sample.tvmaze.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.sample.tvmaze.domain.Show
import com.android.sample.tvmaze.repository.ShowRepository
import com.android.sample.tvmaze.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainViewModel(
        private val repository: ShowRepository
) : ViewModel() {

    private val _shows = MutableStateFlow<Resource<List<Show>>>(Resource.loading())
    val shows: StateFlow<Resource<List<Show>>>
        get() = _shows

    init {
        refreshShows()
    }

    fun refreshShows() {
        viewModelScope.launch {
            repository.result.collect {
                _shows.value = it
            }
        }
    }
}