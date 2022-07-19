package com.example.githubclientkmm.android.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.githubclientkmm.android.R
import com.example.githubclientkmm.android.databinding.AuthFragmentBinding
import com.example.githubclientkmm.android.presentation.auth.AuthViewModel.Action
import com.example.githubclientkmm.android.presentation.auth.AuthViewModel.State
import com.example.githubclientkmm.android.utils.bindTextTwoWay
import com.example.githubclientkmm.android.utils.collectActions
import com.example.githubclientkmm.android.utils.collectLatestLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.auth_fragment) {
    private var _binding: AuthFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = AuthFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindToViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindViewsToState(state: State) {
        with(binding) {
            inputLayout.error =
                if (state is State.InvalidInput) state.reason.getString(requireContext()) else null
            progressCircular.visibility =
                if (state is State.Loading) View.VISIBLE else View.GONE
            submitButton.isEnabled = state !is State.Loading
            submitButton.text =
                if (state is State.Loading) "" else getString(R.string.btn_sign_in)
        }
    }

    private fun bindToViewModel() {
        collectLatestLifecycleFlow(viewModel.state) { state ->
            bindViewsToState(state)
        }
        binding.submitButton.setOnClickListener { viewModel.signButtonPressed() }

        binding.inputEditText.bindTextTwoWay(
            stateFlow = viewModel.token,
            lifecycleOwner = viewLifecycleOwner
        )

        collectActions(viewModel.actions) { action ->
            when (action) {
                is Action.ShowError -> {
                    showErrorDialog(action.message.getString(requireContext()), action.code)
                }
                is Action.RouteToMain -> this.findNavController().navigate(
                    AuthFragmentDirections
                        .toRepositoriesListFragment()
                )
            }
        }
    }

    private fun showErrorDialog(message: String, code: Int? = null) {
        val dialogFragment = ErrorDialogFragment()
        dialogFragment.arguments = ErrorDialogFragment.createArguments(message, code)
        dialogFragment.show(childFragmentManager, ERROR_DIALOG_TAG)
    }

    private companion object {
        const val ERROR_DIALOG_TAG = "ErrorDialogFragment"
    }

}
