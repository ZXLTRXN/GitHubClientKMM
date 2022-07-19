package com.example.githubclientkmm.android.presentation.auth

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.example.githubclientkmm.android.R
import com.example.githubclientkmm.data.network.RequestCode


class ErrorDialogFragment() : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val message: String =
            requireNotNull(requireArguments().getString(MESSAGE_KEY)) {
                "argument $MESSAGE_KEY should be not null"
            }
        val code: Int? = arguments?.getInt(CODE_KEY)
        val alertMessage = defineMessage(message, code)

        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.error_label))
            .setMessage(alertMessage)
            .setPositiveButton(R.string.btn_error_ok) { _, _ -> }
            .create()
    }

    private fun defineMessage(message: String, code: Int?): String {
        val codesForUser = listOf(RequestCode.NO_CONNECTION.code, RequestCode.UNAUTHORIZED.code)
        return if (code == null || code in codesForUser) message
        else "$message / $code"
    }

    companion object {
        private const val MESSAGE_KEY = "message"
        private const val CODE_KEY = "code"

        fun createArguments(message: String, code: Int? = null): Bundle =
            bundleOf(MESSAGE_KEY to message, CODE_KEY to code)
    }
}