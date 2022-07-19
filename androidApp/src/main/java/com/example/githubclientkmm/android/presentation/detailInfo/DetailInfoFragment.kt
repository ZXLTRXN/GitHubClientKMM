package com.example.githubclientkmm.android.presentation.detailInfo

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.githubclientkmm.android.R
import com.example.githubclientkmm.android.databinding.DetailInfoFragmentBinding
import com.example.githubclientkmm.android.presentation.detailInfo.RepositoryInfoViewModel.ReadmeState
import com.example.githubclientkmm.android.presentation.detailInfo.RepositoryInfoViewModel.State
import com.example.githubclientkmm.android.utils.collectLatestLifecycleFlow
import com.example.githubclientkmm.android.utils.setUpToolbar
import com.example.githubclientkmm.android.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon

@AndroidEntryPoint
class DetailInfoFragment : Fragment(R.layout.detail_info_fragment) {
    private var _binding: DetailInfoFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RepositoryInfoViewModel>()

    private val args: DetailInfoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = DetailInfoFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar(
            toolbar = binding.topAppBarLayout.topAppBar,
            title = args.repoName
        ) {
            viewModel.signOutPressed()
        }
        bindToViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindReadmeViewsToState(state: State) {
        with(binding) {
            progressCircularReadme.visibility =
                if (state is State.Loaded && state.readmeState is ReadmeState.Loading) View.VISIBLE else View.GONE

            errorReadmeLayout.root.visibility =
                if (state is State.Loaded && state.readmeState is ReadmeState.Error) View.VISIBLE else View.GONE
            errorReadmeLayout.tvLabelError.text =
                if (state is State.Loaded && state.readmeState is ReadmeState.Error) state.readmeState.errorLabel.getString(
                    requireContext()
                ) else null
            errorReadmeLayout.tvInfoError.text =
                if (state is State.Loaded && state.readmeState is ReadmeState.Error) state.readmeState.errorMessage.getString(
                    requireContext()
                ) else null
            val readmeErrorDrawable: Drawable? =
                if (state is State.Loaded && state.readmeState is ReadmeState.Error) ResourcesCompat.getDrawable(
                    resources,
                    state.readmeState.errorIcon,
                    null
                )
                else null
            errorReadmeLayout.ivError.setImageDrawable(readmeErrorDrawable)

            if (state is State.Loaded && state.readmeState is ReadmeState.Loaded) {
                context?.let { context ->
                    val markwon = Markwon.create(context)
                    markwon.setMarkdown(tvReadme, state.readmeState.markdown)
                }
            } else {
                tvReadme.text = if (state is State.Loaded && state.readmeState is ReadmeState.Empty)
                    getString(R.string.empty_readme) else null
            }
        }
    }

    private fun bindViewsToState(state: State) {
        bindReadmeViewsToState(state)
        with(binding) {
            errorLayout.root.visibility = if (state is State.Error) View.VISIBLE else View.GONE
            errorLayout.tvLabelError.text =
                if (state is State.Error) state.errorLabel.getString(requireContext()) else null
            errorLayout.tvInfoError.text =
                if (state is State.Error) state.errorMessage.getString(requireContext()) else null
            val errorDrawable: Drawable? =
                if (state is State.Error) ResourcesCompat.getDrawable(
                    resources,
                    state.errorIcon,
                    null
                )
                else null
            errorLayout.ivError.setImageDrawable(errorDrawable)

            loadingLayout.root.visibility =
                if (state is State.Loading) View.VISIBLE else View.GONE

            content.visibility = if (state is State.Loaded) View.VISIBLE else View.GONE
            allSections.visibility = if (state is State.Loaded) View.VISIBLE else View.GONE
            tvLicense.text =
                if (state is State.Loaded) state.githubRepo.license
                    ?: getString(R.string.empty_license) else null
            tvStars.text =
                if (state is State.Loaded) state.githubRepo.stars.toString() else null
            tvForks.text =
                if (state is State.Loaded) state.githubRepo.forks.toString() else null
            tvWatchers.text =
                if (state is State.Loaded) state.githubRepo.watchers.toString() else null
            tvLink.text = if (state is State.Loaded) state.githubRepo.htmlUrl else null
            if (state is State.Loaded) {
                tvLink.setOnClickListener { openUrl(state.githubRepo.htmlUrl) }
            } else {
                tvLink.setOnClickListener { }
            }
        }
    }

    private fun bindToViewModel() {
        collectLatestLifecycleFlow(viewModel.state) { state ->
            bindViewsToState(state)
        }
        binding.errorLayout.retryButton.setOnClickListener { viewModel.reloadPressed() }
        binding.errorReadmeLayout.retryButton.setOnClickListener { viewModel.reloadPressed() }
    }

    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(url) }
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            showToast(getString(R.string.browser_unavailable))
        } catch (e: Exception) {
            showToast(getString(R.string.bad_url_repo))
        }
    }
}