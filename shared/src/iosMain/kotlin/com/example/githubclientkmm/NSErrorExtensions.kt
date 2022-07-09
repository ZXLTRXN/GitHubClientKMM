package com.example.githubclientkmm

import com.example.githubclientkmm.data.network.ConnectionException
import com.example.githubclientkmm.data.network.ForbiddenException
import com.example.githubclientkmm.data.network.NotFoundException
import com.example.githubclientkmm.data.network.UnauthorizedException
import com.example.githubclientkmm.data.network.UnknownException
import platform.Foundation.NSError

fun NSError.getKotinException(): CustomError {
    if (this.domain != "KotlinException")
        return CustomError.Unknown(code = 421, body = "asdadadawdad")

    val error = this.userInfo() as? RuntimeException
    return when (error) {
        is UnauthorizedException -> CustomError.Unauthorized(code = 40001, body = "asdaggdadawdad")
        is ForbiddenException -> CustomError.Unauthorized(code = 40002, body = "asdaggdadawdad")
        is NotFoundException -> CustomError.Unauthorized(code = 40003, body = "asdaggdadawdad")
        is ConnectionException -> CustomError.Unauthorized(code = 40004, body = "asdaggdadawdad")
        is UnknownException -> CustomError.Unauthorized(code = 40005, body = "asdaggdadawdad")
        else -> CustomError.Unknown(code = 40006, body = "asdadadawdad")
    }
}

sealed class CustomError(){
    class Unauthorized(val code: Int, val body: String): CustomError()
    class Forbidden(val code: Int, val body: String): CustomError()
    class NotFound(val code: Int, val body: String): CustomError()
    class Unknown(val code: Int, val body: String): CustomError()
    object Connection: CustomError()
}