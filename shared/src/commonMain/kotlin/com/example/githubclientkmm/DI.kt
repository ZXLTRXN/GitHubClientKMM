package com.example.githubclientkmm

import com.example.githubclientkmm.data.AppRepository
import com.example.githubclientkmm.data.KeyValueStorage
import com.example.githubclientkmm.data.network.APIService
import com.example.githubclientkmm.data.network.jsonHttpClient
import com.example.githubclientkmm.data.network.readmeHttpClient
import com.example.githubclientkmm.data.settings

object DI {
    private val api: APIService = APIService(jsonHttpClient, readmeHttpClient)

    val storage: KeyValueStorage = KeyValueStorage(settings)
    val appRepo: AppRepository = AppRepository(api = api, storage = storage)
}