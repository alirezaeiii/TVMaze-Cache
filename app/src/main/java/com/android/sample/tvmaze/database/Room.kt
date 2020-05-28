package com.android.sample.tvmaze.database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Data Access Object for the category table.
 */
@Dao
interface ShowDao {

    /**
     * Select all shows from the shows table.
     *
     * @return all shows.
     */
    @Query("SELECT * FROM databaseshow")
    fun getShows(): LiveData<List<DatabaseShow>>

    /**
     * Insert shows in the database. If the show already exists, replace it.
     *
     * @param shows the shows to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg shows: DatabaseShow)
}

@Database(entities = [DatabaseShow::class], version = 1, exportSchema = false)
abstract class ShowDatabase : RoomDatabase() {
    abstract val showDao: ShowDao
}