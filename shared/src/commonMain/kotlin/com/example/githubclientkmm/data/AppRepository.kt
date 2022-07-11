package com.example.githubclientkmm.data

import com.example.githubclientkmm.data.network.APIService
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class AppRepository {
    private val api = APIService()
    private val storage = KeyValueStorage()

    init {
        Napier.base(DebugAntilog())
    }

//    @Throws(Exception::class)
//    suspend fun getRepositories(): List<Repo> {
//        // TODO:
//    }
//
//    @Throws(Exception::class)
//    suspend fun getRepository(repoId: String): Repo {
//        // TODO:
//    }
//
//    @Throws(Exception::class)
//    suspend fun getRepositoryReadme(
//        ownerName: String,
//        repositoryName: String,
//        branchName: String
//    ): String {
//        // TODO:
//    }

    @Throws(Exception::class)
    suspend fun signIn(token: String) {
        signOut()
        api.getUser(token = token)
        storage.authToken = token
    }

    fun signOut() {
        storage.authToken = null
    }
}