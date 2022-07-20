package com.example.githubclientkmm.android.utils

import com.example.githubclientkmm.android.presentation.auth.AuthViewModel.ValidationState
import java.util.regex.Pattern

fun String.validateToken(): ValidationState {
    val pattern = "^[a-zA-Z0-9_-]{0,45}$"
    return if (this.isEmpty()) ValidationState.Empty
    else {
        val matcher = Pattern.compile(pattern).matcher(this)
        if (matcher.matches()) ValidationState.Valid
        else ValidationState.Invalid
    }

}