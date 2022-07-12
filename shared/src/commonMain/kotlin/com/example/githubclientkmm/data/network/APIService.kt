package com.example.githubclientkmm.data.network

import com.example.githubclientkmm.data.network.models.RepoNetwork
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders

class APIService(private val client: HttpClient) {
    suspend fun getUser(token: String): RepoNetwork.Owner =
        client.get(path = "user") {
            header(HttpHeaders.Authorization, "token $token")
        }

    suspend fun getRepositories(
        sort: String = "updated",
        amount: Int = REPOSITORIES_AMOUNT
    ): List<RepoNetwork> =
        client.get(path = "user/repos") {
            parameter("sort", sort)
            parameter("per_page", amount)
        }

    suspend fun getRepository(
        ownerName: String,
        repoName: String
    ): RepoNetwork =
        client.get(path = "/repos/$ownerName/$repoName")

    suspend fun getRepositoryReadme(
        ownerName: String,
        repoName: String,
        branchName: String
    ): String {
        return "aaaaaa"
    }

    companion object {
        const val BASE_HOST = "api.github.com"
        const val BASE_README_HOST = "raw.githubusercontent.com"
        private const val REPOSITORIES_AMOUNT = 10
    }
}