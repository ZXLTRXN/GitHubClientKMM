package com.example.githubclientkmm.android.presentation.repositoriesList

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import com.example.githubclientkmm.android.R
import com.example.githubclientkmm.android.databinding.FragmentRepositoriesListBinding
import com.example.githubclientkmm.android.presentation.repositoriesList.RepositoriesListViewModel.State
import com.example.githubclientkmm.android.presentation.repositoriesList.recyclerView.RepositoriesAdapter
import com.example.githubclientkmm.android.utils.collectLatestLifecycleFlow
import com.example.githubclientkmm.android.utils.setUpToolbar
import com.example.githubclientkmm.data.models.Repo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepositoriesListFragment : Fragment(R.layout.fragment_repositories_list) {
    private var _binding: FragmentRepositoriesListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RepositoriesListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentRepositoriesListBinding.inflate(inflater, container, false)
        val view = binding.apply {
            rvRepositoriesList.addItemDecoration(DividerItemDecoration(context, VERTICAL))
        }
        return view.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar(toolbar = binding.topAppBarLayout.topAppBar) {
            viewModel.signOut()
        }
        val adapter = setUpAdapter()
        observe(adapter)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvRepositoriesList.adapter = null
        _binding = null
    }


    private fun setUpAdapter(): RepositoriesAdapter {
        val adapter = RepositoriesAdapter { owner, name, branch ->
            navigateToDetailInfo(owner, name, branch)
        }
        binding.rvRepositoriesList.adapter = adapter
        return adapter
    }

    private fun setUpViews(state: State, adapter: RepositoriesAdapter) {
        with(binding) {
            loadingLayout.root.visibility =
                if (state is State.Loading) View.VISIBLE else View.GONE

            val newDataForAdapter: List<Repo> = if (state is State.Loaded) state.repos
            else listOf()
            adapter.submitList(newDataForAdapter)
            rvRepositoriesList.visibility =
                if (state is State.Loaded) View.VISIBLE else View.GONE

            val errorDrawable: Drawable? =
                if (state is State.Error) ResourcesCompat.getDrawable(
                    resources,
                    state.errorIcon, null
                ) else null
            errorLayout.ivError.setImageDrawable(errorDrawable)
            errorLayout.tvLabelError.text =
                if (state is State.Error) state.errorLabel.getString(requireContext()) else null
            errorLayout.tvInfoError.text =
                if (state is State.Error) state.errorMessage.getString(requireContext()) else null
            errorLayout.root.visibility =
                if (state is State.Error) View.VISIBLE else View.GONE

            emptyLayout.root.visibility =
                if (state is State.Empty) View.VISIBLE else View.GONE
            emptyLayout.tvInfoEmpty.text =
                if (state is State.Error) getString(R.string.empty_repositories_list) else null
        }
    }

    private fun observe(adapter: RepositoriesAdapter) {
        collectLatestLifecycleFlow(viewModel.state) { state ->
            setUpViews(state, adapter)
        }
        binding.errorLayout.retryButton.setOnClickListener { viewModel.retry() }
    }

    private fun navigateToDetailInfo(ownerName: String, repoName: String, branch: String) {
        val action = RepositoriesListFragmentDirections
            .toDetailInfoFragment(ownerName = ownerName, repoName = repoName, branch = branch)
        this.findNavController().navigate(action)
    }
}