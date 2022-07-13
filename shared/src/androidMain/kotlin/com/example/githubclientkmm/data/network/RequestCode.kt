package com.example.githubclientkmm.data.network

import io.ktor.http.HttpStatusCode

enum class RequestCode(val code: Int) {
    NO_CONNECTION(0),
    UNAUTHORIZED(HttpStatusCode.Unauthorized.value),
    FORBIDDEN(HttpStatusCode.Forbidden.value),
    NOT_FOUND(HttpStatusCode.NotFound.value)
}