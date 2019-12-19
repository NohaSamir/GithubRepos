package com.example.githubrepos

import android.content.Context
import com.example.githubrepos.database.ReposDatabase
import com.example.githubrepos.database.ReposDatabaseDao
import com.example.githubrepos.network.ReposApi
import com.example.githubrepos.repository.GithubRepository
import kotlinx.coroutines.CoroutineScope

object Injection {

    private fun provideCache(context: Context): ReposDatabaseDao {
        return ReposDatabase.getInstance(context).reposDatabaseDao
    }


    fun provideGithubRepository(
        context: Context,
        coroutineScope: CoroutineScope
    ): GithubRepository {
        return GithubRepository(ReposApi.githubApis, provideCache(context), coroutineScope)
    }

}