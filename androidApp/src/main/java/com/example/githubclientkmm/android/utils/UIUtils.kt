package com.example.githubclientkmm.android.utils


import com.example.githubclientkmm.android.R
import com.example.githubclientkmm.data.network.RequestCode


fun makeErrorMessage(requestCode: RequestCode?): Pair<Int, LocalizeString> {
    return when (requestCode) {
        RequestCode.NO_CONNECTION ->
            R.drawable.ic_network_error to LocalizeString.Resource(R.string.network_error_label)
        else ->
            R.drawable.ic_something_error to LocalizeString.Resource(R.string.something_error_label)
    }
}