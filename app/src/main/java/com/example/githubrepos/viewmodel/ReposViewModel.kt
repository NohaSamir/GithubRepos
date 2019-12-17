package com.example.githubrepos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubrepos.repository.ReposRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ReposViewModel(private val repository: ReposRepository) : ViewModel() {

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var since = 0


    init {
        loadRepos()
    }

    val repos = repository.repositories

    fun loadRepos() {
        viewModelScope.launch {
            repository.getRepos(since)
            if(repos.value != null && repos.value!!.isNotEmpty())
                since = repos.value?.get(repos.value!!.size - 1)?.id ?: 0
        }
    }

    /**
     * Cancel all coroutines when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /**
     * Factory for constructing ReposViewModel with parameter
     */
    class Factory(private val repository: ReposRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ReposViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ReposViewModel(repository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}