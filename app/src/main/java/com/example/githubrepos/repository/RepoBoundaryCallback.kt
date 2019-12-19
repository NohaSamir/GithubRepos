package com.example.githubrepos.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.example.githubrepos.database.ReposDatabaseDao
import com.example.githubrepos.domain.Repo
import com.example.githubrepos.network.GithubApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RepoBoundaryCallback(
    private val network: GithubApiService,
    private val cache: ReposDatabaseDao,
    private val coroutineScope: CoroutineScope
) : PagedList.BoundaryCallback<Repo>() {


    // keep the last requested repo. When the request is successful, we save the last one.
    private var since = 0

    private val _networkErrors = MutableLiveData<String>()
    // LiveData of network errors.
    val networkErrors: LiveData<String>
        get() = _networkErrors

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    /**
     * Database returned 0 items. We should query the backend for more items.
     */
    override fun onZeroItemsLoaded() {
        Log.d("RepoBoundaryCallback", "onZeroItemsLoaded")
        requestAndSaveData()
    }

    /**
     * When all items in the database were loaded, we need to query the backend for more items.
     */
    override fun onItemAtEndLoaded(itemAtEnd: Repo) {
        Log.d("RepoBoundaryCallback", "onItemAtEndLoaded")
        requestAndSaveData()
    }

    fun requestAndSaveData() {
        if (isRequestInProgress) return

        isRequestInProgress = true

        coroutineScope.launch {

            withContext(Dispatchers.IO) {
                try {
                    val repos = network.getReposAsync(since).await()
                    isRequestInProgress = false
                    if (repos.isNotEmpty()) since = repos[repos.lastIndex].id
                    cache.insertAll(repos)
                }catch (e: Exception)
                {
                    _networkErrors.postValue(e.message)
                }
            }

        }
    }
}