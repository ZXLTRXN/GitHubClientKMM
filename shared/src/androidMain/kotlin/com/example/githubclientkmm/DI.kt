package com.example.githubclientkmm

import com.example.githubclientkmm.data.AppRepository
import com.example.githubclientkmm.data.KeyValueStorage
import com.example.githubclientkmm.data.network.APIService
import com.example.githubclientkmm.data.network.ktorHttpClient
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient

object DI {
    private val settings: Settings = Settings()
    private val client: HttpClient = ktorHttpClient
    private val api: APIService =  APIService(client)

    val storage: KeyValueStorage = KeyValueStorage(settings)
    val appRepo: AppRepository = AppRepository(api = api, storage = storage)
}