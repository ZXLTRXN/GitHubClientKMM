package com.example.githubclientkmm.android.presentation.repositoriesList.recyclerView

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.githubclientkmm.android.databinding.RepositoriesListElementBinding
import com.example.githubclientkmm.data.models.Repo


class RepositoriesAdapter(
    private val onItemClick: (String, String, String) -> Unit
) : ListAdapter<Repo, RepositoriesAdapter.RepoViewHolder>(RepoDiffUtilCallback()) {

    class RepoViewHolder(
        private val binding: RepositoriesListElementBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(repo: Repo) {
            bindName(repo.name)
            bindLanguage(repo.language, repo.languageColor)
            bindDescription(repo.description)
        }

        private fun bindName(text: String?) {
            binding.repoName.text = text
        }

        private fun bindLanguage(text: String?, color: String?) {
            val defaultColorId: Int = binding.repoName.currentTextColor
            val resultColorId: Int = try {
                if (color == null) defaultColorId
                else Color.parseColor(color)
            } catch (e: IllegalArgumentException) {
                defaultColorId
            }
            binding.repoLanguage.text = text
            binding.repoLanguage.setTextColor(resultColorId)
        }

        private fun bindDescription(text: String?) {
            binding.repoDescription.visibility = if (text == null) View.GONE else View.VISIBLE
            binding.repoDescription.text = text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val view = RepositoriesListElementBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return RepoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val repo = getItem(position)

        holder.bind(repo)
        holder.itemView.setOnClickListener {
            onItemClick(repo.owner, repo.name, repo.branch)
        }
    }
}