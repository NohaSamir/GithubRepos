package com.example.githubrepos.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.githubrepos.domain.Repo

/**
 * Defines methods for using the SleepNight class with Room.
 */
@Dao
interface ReposDatabaseDao {

    /**
     * Insert all values in the table.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(repos: List<Repo>)


    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM repositories_table")
    fun clear()

    /**
     * Selects and returns all rows in the table,
     *
     * sorted by id in ascending order.
     */
    @Query("SELECT * FROM repositories_table ORDER BY id ASC")
    fun getAllRepos(): LiveData<List<Repo>>


}

