package com.android.sample.tvmaze.util.contextProvider

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

open class CoroutineContextProvider {
    open val main: CoroutineContext = Dispatchers.Main
    open val io: CoroutineContext = Dispatchers.IO
}