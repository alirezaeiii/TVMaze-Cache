package com.android.sample.tvmaze.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.sample.tvmaze.domain.Show
import com.android.sample.tvmaze.repository.ShowRepository
import com.android.sample.tvmaze.util.Result
import com.android.sample.tvmaze.util.isNetworkAvailable
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: ShowRepository,
    app: Application
) : AndroidViewModel(app) {

    private val _shows = repository.shows
    val shows: LiveData<List<Show>>
        get() = _shows

    private val _liveData = MutableLiveData<Result<List<Show>>>()
    val liveData: LiveData<Result<List<Show>>>
        get() = _liveData

    /**
     * init{} is called immediately when this ViewModel is created.
     */
    init {
        if (isNetworkAvailable(app)) {
            viewModelScope.launch {
                _liveData.postValue(repository.refreshShows())
            }
        }
    }
}