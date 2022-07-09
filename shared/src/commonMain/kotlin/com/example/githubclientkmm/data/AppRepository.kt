package com.example.githubclientkmm.data

import com.example.githubclientkmm.data.models.Repo
import com.example.githubclientkmm.data.network.APIService
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.network.sockets.SocketTimeoutException

class AppRepository {
    private val api = APIService()

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
    suspend fun signIn(token: String): Unit {
        val storage = KeyValueStorage()
        storage.authToken = null
        try {
            api.getUser(token = token)
        } catch (e: RuntimeException) {
            Napier.e("error", e, "AAA")
            throw e
        }
    }

}