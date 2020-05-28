package com.android.sample.tvmaze.network

import com.android.sample.tvmaze.domain.Show
import retrofit2.Call
import retrofit2.http.GET

interface TVMazeService {

    @GET("shows")
    fun fetchShowList(): Call<List<Show>>
}