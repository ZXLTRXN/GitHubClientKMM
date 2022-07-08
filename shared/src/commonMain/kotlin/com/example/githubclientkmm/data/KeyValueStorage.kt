package com.example.githubclientkmm.data

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set

class KeyValueStorage {
    var authToken: String?
        get() = settings.getStringOrNull(TOKEN_KEY)
        set(value) {
            settings[TOKEN_KEY] = value
        }

    private val settings: Settings = Settings()

    companion object {
        private const val TOKEN_KEY = "token"
    }
}