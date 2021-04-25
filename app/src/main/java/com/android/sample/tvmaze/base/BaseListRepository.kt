package com.android.sample.tvmaze.base

import android.content.Context
import com.android.sample.tvmaze.util.contextProvider.CoroutineContextProvider

abstract class BaseListRepository<T>(
        context: Context,
        contextProvider: CoroutineContextProvider
) : BaseRepository<List<T>>(context, contextProvider) {

    override fun isNotEmpty(it: List<T>): Boolean = it.isNotEmpty()
}