package com.example.githubclientkmm.android.utils


import com.example.githubclientkmm.android.R
import com.example.githubclientkmm.data.network.ConnectionException
import com.example.githubclientkmm.data.network.ForbiddenException
import com.example.githubclientkmm.data.network.RequestCode
import com.example.githubclientkmm.data.network.UnauthorizedException
import com.example.githubclientkmm.data.network.UnknownException
import com.example.githubclientkmm.data.network.UnknownRequestException


fun makeDevelopersErrorMessage(exception: Exception): Pair<LocalizeString, Int?> {
    return try {
        throw exception
    } catch (e: ConnectionException) {
        LocalizeString.Resource(R.string.network_error) to RequestCode.NO_CONNECTION.code
    } catch (e: UnauthorizedException) {
        LocalizeString.Resource(R.string.wrong_token) to RequestCode.UNAUTHORIZED.code
    } catch (e: UnknownRequestException) {
        LocalizeString.Resource(R.string.unknown_error) to e.code
    } catch (e: UnknownException) {
        LocalizeString.Raw(exception.cause!!.message!!) to null
    } catch (e: Exception) {
        LocalizeString.Raw(exception.message!!) to null
    }
}

fun makeErrorMessage(exception: Exception): Triple<Int, LocalizeString, LocalizeString> {
    return try {
        throw  exception
    } catch (e: ConnectionException) {
        Triple(
            R.drawable.ic_network_error,
            LocalizeString.Resource(R.string.network_error_label),
            LocalizeString.Resource(R.string.network_error)
        )
    } catch (e: ForbiddenException) {
        Triple(
            R.drawable.ic_something_error,
            LocalizeString.Resource(R.string.something_error_label),
            LocalizeString.Resource(R.string.no_rights)
        )
    } catch (e: Exception) {
        Triple(
            R.drawable.ic_something_error,
            LocalizeString.Resource(R.string.something_error_label),
            LocalizeString.Resource(R.string.something_error)
        )
    }
}