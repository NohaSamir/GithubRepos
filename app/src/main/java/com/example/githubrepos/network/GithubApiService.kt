package com.example.githubrepos.network

import com.example.githubrepos.domain.Repo
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface GithubApiService {

    @GET("repositories")
    fun getReposAsync(@Query("since") since: Int): Call<List<Repo>>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object ReposApi {
    val githubApis: GithubApiService by lazy { retrofit.create(GithubApiService::class.java) }
}
