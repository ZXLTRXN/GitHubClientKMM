package com.example.githubclientkmm.data

import com.example.githubclientkmm.data.models.Repo

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
