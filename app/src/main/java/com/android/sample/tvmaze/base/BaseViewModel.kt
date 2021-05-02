package com.android.sample.tvmaze.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.sample.tvmaze.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

open class BaseViewModel<T>(
        private val repository: BaseRepository<T>
) : ViewModel() {

    private val _shows = MutableStateFlow<Resource<T>>(Resource.loading())
    val shows: StateFlow<Resource<T>>
        get() = _shows

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            repository.result.collect {
                _shows.value = it
            }
        }
    }
}