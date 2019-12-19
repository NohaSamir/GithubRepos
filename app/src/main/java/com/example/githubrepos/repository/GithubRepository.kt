package com.example.githubrepos.repository

import androidx.paging.LivePagedListBuilder
import com.example.githubrepos.database.ReposDatabaseDao
import com.example.githubrepos.domain.RepoResult
import com.example.githubrepos.network.GithubApiService
import kotlinx.coroutines.CoroutineScope


class GithubRepository(
    private val network: GithubApiService,
    private val cache: ReposDatabaseDao,
    private val coroutineScope: CoroutineScope
) {

    //val repositories: LiveData<List<Repo>> = cache.getAllRepos()

    /* suspend fun getRepos(since: Int): RepoResult {
         withContext(Dispatchers.IO) {
             val repos = network.getReposAsync(since).await()
             Log.d("Response", "$since $repos")
             cache.insertAll(repos)
         }
     }*/

    fun getRepos(): RepoResult {
        // Get data source factory from the local cache
        val dataSourceFactory = cache.getAllRepos()

        // every new query creates a new BoundaryCallback
        // The BoundaryCallback will observe when the user reaches to the edges of
        // the list and update the database with extra data
        val boundaryCallback = RepoBoundaryCallback(network, cache, coroutineScope)
        val networkErrors = boundaryCallback.networkErrors

        // Get the paged list
        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback)
            .build()

        // Get the network errors exposed by the boundary callback
        return RepoResult(data, networkErrors)
    }


    companion object {
        private const val DATABASE_PAGE_SIZE = 20
    }
}
