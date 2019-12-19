package com.example.githubrepos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import com.example.githubrepos.Injection
import com.example.githubrepos.domain.Repo
import com.example.githubrepos.domain.RepoResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class ReposViewModel(application: Application) : AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val repository by lazy {
        Injection.provideGithubRepository(application, viewModelScope)
    }

    private val _repoResult = MutableLiveData<RepoResult>()

    val repos: LiveData<PagedList<Repo>> = Transformations.switchMap(_repoResult) { it -> it.data }
    val networkErrors: LiveData<String> = Transformations.switchMap(_repoResult) { it ->
        it.networkErrors
    }


    init {
        _repoResult.postValue(repository.getRepos())
    }

    /**
     * Cancel all coroutines when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}