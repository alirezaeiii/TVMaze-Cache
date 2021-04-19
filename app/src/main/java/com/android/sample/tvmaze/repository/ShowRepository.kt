package com.android.sample.tvmaze.repository

import android.content.Context
import com.android.sample.tvmaze.database.ShowDao
import com.android.sample.tvmaze.database.asDomainModel
import com.android.sample.tvmaze.domain.Show
import com.android.sample.tvmaze.domain.asDatabaseModel
import com.android.sample.tvmaze.network.TVMazeService
import com.android.sample.tvmaze.util.contextProvider.CoroutineContextProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow

@ExperimentalCoroutinesApi
class ShowRepository(
        private val dao: ShowDao,
        private val api: TVMazeService,
        context: Context,
        contextProvider: CoroutineContextProvider
) : BaseRepository<List<Show>>(context, contextProvider) {

    override suspend fun query(): Pair<Boolean, List<Show>> {
        val showsFromDb = dao.getShows()
        return Pair(showsFromDb.isEmpty(), showsFromDb.asDomainModel())
    }

    override suspend fun fetch(): List<Show> = api.fetchShowList()

    override suspend fun saveFetchResult(requestType: List<Show>) {
        dao.insertAll(*requestType.asDatabaseModel())
    }
}