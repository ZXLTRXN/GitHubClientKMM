package com.example.githubclientkmm.data.network

class UnauthorizedException(
    val code: Int,
    val body: String,
    cause: Throwable
) : RuntimeException(cause)

class ForbiddenException(
    val code: Int,
    val body: String,
    cause: Throwable
) : RuntimeException(cause)

class ConnectionException(
    cause: Throwable
) : RuntimeException(cause)

class NotFoundException(
    val code: Int,
    val body: String,
    cause: Throwable
) : RuntimeException(cause)

class UnknownRequestException(
    val code: Int,
    val body: String,
    cause: Throwable
) : RuntimeException("Unknown status code: $code in http response with body: $body", cause)

class UnknownException(
    cause: Throwable
) : RuntimeException("Unknown Kotlin exception", cause)