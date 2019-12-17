package com.example.githubrepos.domain

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "repositories_table")
data class Repo(
    @PrimaryKey
    val id: Int,
    val title: String?,
    val description: String?,
    @Json(name = "created_at")
    val creationDate: String?,
    val language: String?,
    @Json(name = "forks_count")
    val forksCount: Int?,
    @Json(name = "html_url")
    val url: String?,
    @Embedded
    val owner: Owner?
)

data class Owner(
    @Json(name = "avatar_url")
    val avatarUrl: String?
)

