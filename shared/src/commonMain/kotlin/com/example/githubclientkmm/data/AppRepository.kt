package com.example.githubclientkmm.data

import com.example.githubclientkmm.data.models.Repo
import com.example.githubclientkmm.data.network.APIService
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class AppRepository(
    private val api: APIService,
    private val storage: KeyValueStorage
) {

    init {
        Napier.base(DebugAntilog())
    }

    @Throws(Exception::class)
    suspend fun getRepositories(): List<Repo> =
        api.getRepositories().map { it.asRepo() }

    @Throws(Exception::class)
    suspend fun getRepository(ownerName: String, repoName: String): Repo =
        api.getRepository(ownerName, repoName).asRepo()


    @Throws(Exception::class)
    suspend fun getRepositoryReadme(
        ownerName: String,
        repoName: String,
        branchName: String
    ): String = api.getRepositoryReadme(ownerName, repoName, branchName)


    @Throws(Exception::class)
    suspend fun signIn(token: String) {
        storage.authToken = null
        api.getUser(token = token)
        storage.authToken = token
    }
}