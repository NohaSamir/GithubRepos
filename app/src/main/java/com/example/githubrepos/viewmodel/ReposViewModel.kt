package com.example.githubrepos.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.example.githubrepos.Injection
import com.example.githubrepos.domain.Repo
import com.example.githubrepos.domain.RepoResult
import com.example.githubrepos.repository.GithubRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

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
        loadRepos()
    }

    //val repos = repository.getRepos()

    fun loadRepos() {
        viewModelScope.launch {
            _repoResult.postValue(repository.getRepos())
        }
    }

    /**
     * Cancel all coroutines when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}