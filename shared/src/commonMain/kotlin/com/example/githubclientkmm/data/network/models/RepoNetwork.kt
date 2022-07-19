package com.example.githubclientkmm.data.network.models

import com.example.githubclientkmm.data.models.Repo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class RepoNetwork(
    val id: Int,
    val name: String,
    val owner: Owner,
    @SerialName("html_url")
    val htmlUrl: String,
    val description: String? = null,
    val language: String? = null,
    val license: License? = null,
    @SerialName("forks_count")
    val forks: Int = 0,
    @SerialName("stargazers_count")
    val stars: Int = 0,
    @SerialName("watchers_count")
    val watchers: Int = 0,
    @SerialName("default_branch")
    val branch: String = "master",
) {
    @Serializable
    data class License(val name: String)

    @Serializable
    data class Owner(
        @SerialName("login")
        val name: String
    )

    fun asRepo(): Repo = Repo(
        id = this.id,
        owner = this.owner.name,
        name = this.name,
        htmlUrl = this.htmlUrl,
        description = this.description,
        language = this.language,
        license = this.license?.name,
        forks = this.forks,
        stars = this.stars,
        watchers = this.watchers,
        branch = this.branch
    )
}
