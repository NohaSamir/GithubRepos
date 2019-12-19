package com.example.githubrepos.domain

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

/**
 * RepoResult, which contains LiveData<List<Repo>> holding query data,
 * and a LiveData<String> of network error state.
 */
data class RepoResult(
    val data: LiveData<PagedList<Repo>>,
    val networkErrors: LiveData<String>
)
