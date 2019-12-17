package com.example.githubrepos.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.githubrepos.database.ReposDatabase
import com.example.githubrepos.domain.Repo
import com.example.githubrepos.network.GithubApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ReposRepository(
    private val network: GithubApiService,
    private val database: ReposDatabase
) {

    val repositories: LiveData<List<Repo>> = database.reposDatabaseDao.getAllRepos()


    /**
     * Refresh the repos stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     */
    suspend fun getRepos(since: Int) {
        withContext(Dispatchers.IO) {
            val repos = network.getReposAsync(since).await()
            Log.d("Response", "$since $repos")
            database.reposDatabaseDao.insertAll(repos)
        }
    }
}
