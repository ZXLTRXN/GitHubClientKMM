package com.example.githubclientkmm

import com.example.githubclientkmm.data.network.ConnectionException
import com.example.githubclientkmm.data.network.ForbiddenException
import com.example.githubclientkmm.data.network.NotFoundException
import com.example.githubclientkmm.data.network.UnauthorizedException
import com.example.githubclientkmm.data.network.UnknownException
import com.example.githubclientkmm.data.network.UnknownRequestException
import platform.Foundation.NSError

sealed class CustomError {
    class Unauthorized(val code: Int, val body: String): CustomError()
    class Forbidden(val code: Int, val body: String): CustomError()
    class NotFound(val code: Int, val body: String): CustomError()
    class UnknownRequest(val code: Int, val body: String): CustomError()
    class Unknown(val message: String): CustomError()
    object Connection: CustomError()
}

fun NSError.getKotlinException(): CustomError? {
    if (this.domain != "KotlinException") return null
    return when (val e = this.userInfo["KotlinException"] as Throwable) {
        is UnauthorizedException -> CustomError.Unauthorized(code = e.code, body = e.body)
        is ForbiddenException -> CustomError.Forbidden(code = e.code, body = e.body)
        is NotFoundException -> CustomError.NotFound(code = e.code, body = e.body)
        is UnknownRequestException -> CustomError.UnknownRequest(code = e.code, body = e.body)
        is UnknownException -> CustomError.Unknown(message = e.cause!!.message!!)
        is ConnectionException -> CustomError.Connection
        else -> CustomError.Unknown(message = e.message!!)
    }
}
