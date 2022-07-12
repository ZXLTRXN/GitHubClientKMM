package com.example.githubclientkmm.data.network

import com.example.githubclientkmm.data.KeyValueStorage
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.features.HttpResponseValidator
import io.ktor.client.features.ResponseException
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.host
import io.ktor.client.statement.readText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
//import java.net.SocketTimeoutException
//import java.net.UnknownHostException
import kotlinx.serialization.json.Json

val jsonHttpClient: HttpClient
    get() = HttpClient {
        expectSuccess = true
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json {
                ignoreUnknownKeys = true
            })
        }
        defaultRequest(getRequestBuilder(APIService.BASE_HOST))
        HttpResponseValidator {
            handleResponseException(exceptionHandler)
        }
    }

val readmeHttpClient: HttpClient
    get() = HttpClient {
        expectSuccess = true
        defaultRequest(getRequestBuilder(APIService.BASE_README_HOST))
        HttpResponseValidator {
            handleResponseException(exceptionHandler)
        }
    }


private fun getRequestBuilder(baseHost: String): HttpRequestBuilder.() -> Unit = {
    url.protocol = URLProtocol.HTTPS
    host = baseHost
    header(HttpHeaders.Accept, "application/vnd.github.v3+json")
    KeyValueStorage(Settings()).authToken?.let { token ->
        header(HttpHeaders.Authorization, "token $token")
    }
}

private val exceptionHandler: suspend (Throwable) -> Unit = { exception ->
//                if (exception is UnknownHostException
//                    || exception is SocketTimeoutException
//                ) throw ConnectionException(exception)
    if (exception !is ResponseException) throw UnknownException(
        cause = exception
    )
    val response = exception.response
    when (response.status) {
        HttpStatusCode.NotFound -> throw NotFoundException(
            code = response.status.value,
            body = response.readText(),
            cause = exception
        )
        HttpStatusCode.Unauthorized -> throw UnauthorizedException(
            code = response.status.value,
            body = response.readText(),
            cause = exception
        )
        HttpStatusCode.Forbidden -> throw ForbiddenException(
            code = response.status.value,
            body = response.readText(),
            cause = exception
        )
        else -> throw UnknownRequestException(
            code = response.status.value,
            body = response.readText(),
            cause = exception
        )
    }
}