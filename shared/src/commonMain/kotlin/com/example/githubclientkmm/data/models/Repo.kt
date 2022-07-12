package com.example.githubclientkmm.data.models

data class Repo(
    val id: Int,
    val owner: String,
    val name: String,
    val htmlUrl: String,
    val description: String? = null,
    val language: String? = null,
    val license: String? = null,
    val languageColor: String = "#FFFFFF",
    val forks: Int = 0,
    val stars: Int = 0,
    val watchers: Int = 0,
    val branch: String = "master",
)
