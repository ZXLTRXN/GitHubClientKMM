package com.example.githubclientkmm.data.network

import com.example.githubclientkmm.data.network.models.RepoNetwork
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders

class APIService(
    private val jsonClient: HttpClient,
    private val readmeClient: HttpClient
) {
    suspend fun getUser(token: String): RepoNetwork.Owner =
        jsonClient.get(path = "user") {
            header(HttpHeaders.Authorization, "token $token")
        }

    suspend fun getRepositories(
        sort: String = "updated",
        amount: Int = REPOSITORIES_AMOUNT
    ): List<RepoNetwork> =
        jsonClient.get(path = "user/repos") {
            parameter("sort", sort)
            parameter("per_page", amount)
        }

    suspend fun getRepository(
        ownerName: String,
        repoName: String
    ): RepoNetwork =
        jsonClient.get(path = "/repos/$ownerName/$repoName")

    suspend fun getRepositoryReadme(
        ownerName: String,
        repoName: String,
        branchName: String
    ): String {
        return readmeClient.get("$ownerName/$repoName/$branchName/README.md")
    }

    companion object {
        const val BASE_HOST = "api.github.com"
        const val BASE_README_HOST = "raw.githubusercontent.com"
        private const val REPOSITORIES_AMOUNT = 10
    }
}