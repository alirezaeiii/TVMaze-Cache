package com.android.sample.tvmaze.repository

import android.content.Context
import com.android.sample.tvmaze.base.BaseListRepository
import com.android.sample.tvmaze.database.ShowDao
import com.android.sample.tvmaze.database.asDomainModel
import com.android.sample.tvmaze.domain.Show
import com.android.sample.tvmaze.domain.asDatabaseModel
import com.android.sample.tvmaze.network.TVMazeService

class ShowRepository(
        private val dao: ShowDao,
        private val api: TVMazeService,
        context: Context
) : BaseListRepository<Show>(context) {

    override suspend fun query(): List<Show> = dao.getShows().asDomainModel()

    override suspend fun fetch(): List<Show> = api.fetchShowList()

    override suspend fun saveFetchResult(items: List<Show>) {
        dao.insertAll(*items.asDatabaseModel())
    }
}