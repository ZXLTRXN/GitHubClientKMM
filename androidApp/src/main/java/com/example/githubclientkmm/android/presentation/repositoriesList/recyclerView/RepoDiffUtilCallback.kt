package com.example.githubclientkmm.android.presentation.repositoriesList.recyclerView

import androidx.recyclerview.widget.DiffUtil
import com.example.githubclientkmm.data.models.Repo


class RepoDiffUtilCallback : DiffUtil.ItemCallback<Repo>() {
    override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
        return oldItem == newItem
    }

}