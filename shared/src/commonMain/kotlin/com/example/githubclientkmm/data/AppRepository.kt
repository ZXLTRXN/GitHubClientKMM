package com.example.githubclientkmm.data

import com.example.githubclientkmm.data.models.Repo
import com.example.githubclientkmm.data.network.APIService
import com.example.githubclientkmm.data.network.ConnectionException
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.utils.io.errors.IOException

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
    ): String =
        api.getRepositoryReadme(ownerName, repoName, branchName)
//        throw ConnectionException(IOException("aaaaaa"))


    @Throws(Exception::class)
    suspend fun signIn(token: String) {
        storage.authToken = null
        api.getUser(token = token)
        storage.authToken = token
    }
}