package com.example.githubclientkmm.android.utils


import com.example.githubclientkmm.android.R
import com.example.githubclientkmm.data.network.ConnectionException
import com.example.githubclientkmm.data.network.ForbiddenException
import com.example.githubclientkmm.data.network.RequestCode
import com.example.githubclientkmm.data.network.UnauthorizedException
import com.example.githubclientkmm.data.network.UnknownException
import com.example.githubclientkmm.data.network.UnknownRequestException


fun makeDevelopersErrorMessage(exception: Throwable): Pair<LocalizeString, Int?> {
    return when (exception) {
        is ConnectionException -> {
            LocalizeString.Resource(R.string.network_error) to RequestCode.NO_CONNECTION.code
        }
        is UnauthorizedException -> {
            LocalizeString.Resource(R.string.wrong_token) to RequestCode.UNAUTHORIZED.code
        }
        is UnknownRequestException -> {
            LocalizeString.Resource(R.string.unknown_error) to exception.code
        }
        is UnknownException -> {
            LocalizeString.Raw(exception.cause!!.message!!) to null
        }
        else -> {
            LocalizeString.Raw(exception.message!!) to null
        }
    }
}


fun makeErrorMessage(exception: Throwable): Triple<Int, LocalizeString, LocalizeString> {
    return when (exception) {
        is ConnectionException ->
            Triple(
                R.drawable.ic_network_error,
                LocalizeString.Resource(R.string.network_error_label),
                LocalizeString.Resource(R.string.network_error)
            )
        is ForbiddenException ->
            Triple(
                R.drawable.ic_something_error,
                LocalizeString.Resource(R.string.something_error_label),
                LocalizeString.Resource(R.string.no_rights)
            )
        else ->
            Triple(
                R.drawable.ic_something_error,
                LocalizeString.Resource(R.string.something_error_label),
                LocalizeString.Resource(R.string.something_error)
            )
    }
}