package com.android.sample.tvmaze.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.android.sample.tvmaze.domain.Show
import com.android.sample.tvmaze.repository.ShowRepository
import com.android.sample.tvmaze.util.isNetworkAvailable
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(
    private val repository: ShowRepository,
    app: Application
) : AndroidViewModel(app) {

    private val _shows = repository.shows
    val shows: LiveData<List<Show>>
        get() = _shows

    /**
     * init{} is called immediately when this ViewModel is created.
     */
    init {
        if (isNetworkAvailable(app)) {
            viewModelScope.launch {
                repository.refreshShows()
            }
        }
    }
}