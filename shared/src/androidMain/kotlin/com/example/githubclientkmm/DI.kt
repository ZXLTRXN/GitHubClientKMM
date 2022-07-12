package com.example.githubclientkmm

import com.example.githubclientkmm.data.AppRepository
import com.example.githubclientkmm.data.KeyValueStorage
import com.example.githubclientkmm.data.network.APIService
import com.example.githubclientkmm.data.network.jsonHttpClient
import com.example.githubclientkmm.data.network.readmeHttpClient
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient

object DI {
    private val settings: Settings = Settings()
    private val jsonClient: HttpClient = jsonHttpClient
    private val defaultClient: HttpClient = readmeHttpClient
    private val api: APIService = APIService(jsonClient)

    val storage: KeyValueStorage = KeyValueStorage(settings)
    val appRepo: AppRepository = AppRepository(api = api, storage = storage)
}