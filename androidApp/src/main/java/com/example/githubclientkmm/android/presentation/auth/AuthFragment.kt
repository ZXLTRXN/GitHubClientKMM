package com.example.githubclientkmm.android.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.githubclientkmm.android.R
import com.example.githubclientkmm.android.databinding.FragmentAuthBinding
import com.example.githubclientkmm.android.presentation.auth.AuthViewModel.Action
import com.example.githubclientkmm.android.presentation.auth.AuthViewModel.State
import com.example.githubclientkmm.android.utils.bindTextTwoWay
import com.example.githubclientkmm.android.utils.collectActions
import com.example.githubclientkmm.android.utils.collectLatestLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_auth) {
    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        setHasOptionsMenu(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpViews(state: State) {
        with(binding) {
            inputLayout.error =
                if (state is State.InvalidInput) state.reason.getString(requireContext()) else null
            progressCircular.visibility =
                if (state is State.Loading) View.VISIBLE else View.GONE
            submitButton.isEnabled = state !is State.Loading
            submitButton.setOnClickListener { viewModel.onSignButtonPressed() }
            submitButton.text =
                if (state is State.Loading) "" else getString(R.string.btn_sign_in)
        }
    }

    private fun observe() {
        collectLatestLifecycleFlow(viewModel.state) { state ->
            setUpViews(state)
        }

        binding.inputEditText.bindTextTwoWay(
            stateFlow = viewModel.token,
            lifecycleOwner = viewLifecycleOwner
        )

        collectActions(viewModel.actions) { action ->
            when (action) {
                is Action.ShowError ->{
                    context?.let {
                        showErrorDialog(action.message.getString(it), action.code?.code)
                    }
                }
                is Action.RouteToMain -> navigateToRepositoriesList()
            }
        }
    }

    private fun showErrorDialog(message: String, code: Int? = null){
        val args: Bundle = ErrorDialogFragment.createArguments(message, code)
        val dialogFragment = ErrorDialogFragment()
        dialogFragment.arguments = args
        dialogFragment.show(childFragmentManager, ERROR_DIALOG_TAG)
    }

    private fun navigateToRepositoriesList() {
        val action = AuthFragmentDirections
            .toRepositoriesListFragment()
        this.findNavController().navigate(action)
    }
    companion object{
        private const val ERROR_DIALOG_TAG = "ErrorDialogFragment"
    }

}
