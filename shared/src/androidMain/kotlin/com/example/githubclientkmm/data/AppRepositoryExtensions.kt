package com.example.githubclientkmm.data

import com.example.githubclientkmm.data.models.Repo
import com.example.githubclientkmm.data.network.ConnectionException
import com.example.githubclientkmm.data.network.UnknownException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend fun AppRepository.signInResult(token: String): Result<Unit> =
    runCatching { signIn(token) }

suspend fun AppRepository.getRepositoryResult(ownerName: String, repoName: String): Result<Repo> =
    runCatching { getRepository(ownerName, repoName) }

suspend fun AppRepository.getRepositoriesResult(): Result<List<Repo>> =
    runCatching { getRepositories() }

suspend fun AppRepository.getRepositoryReadmeResult(
    ownerName: String,
    repoName: String,
    branchName: String
): Result<String> = runCatching { getRepositoryReadme(ownerName, repoName, branchName) }



private suspend fun <T> AppRepository.catchNetworkException(block: suspend () -> T): T {
    try {
        return block()
    } catch (e: IOException) {
        when (e) {
            is UnknownHostException,
            is SocketTimeoutException-> throw ConnectionException(cause = e)
            else -> throw UnknownException(cause = e)
        }
    }
}

private suspend fun <T> AppRepository.runCatching(block: suspend () -> T): Result<T> =
    kotlin.runCatching {
        this.catchNetworkException { block() }
    }
