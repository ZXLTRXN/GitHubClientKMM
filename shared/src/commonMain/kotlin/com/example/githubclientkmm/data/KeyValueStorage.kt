package com.example.githubclientkmm.data

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set

class KeyValueStorage(private val settings: Settings) {
    var authToken: String?
        get() = settings.getStringOrNull(TOKEN_KEY)
        set(value) {
            settings[TOKEN_KEY] = value
        }

    private companion object {
        const val TOKEN_KEY = "token"
    }
}