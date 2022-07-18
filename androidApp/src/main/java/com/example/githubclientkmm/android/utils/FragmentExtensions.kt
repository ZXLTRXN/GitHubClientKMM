package com.example.githubclientkmm.android.utils

import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.githubclientkmm.android.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun <T> Fragment.collectLatestLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }
    }
}

fun <T> Fragment.collectActions(actions: Flow<T>, handleAction: (T) -> Unit) {
    lifecycleScope.launch {
        actions.collect { handleAction(it) }
    }
}

fun Fragment.setUpToolbar(toolbar: Toolbar, title: String? = null, signOut:() -> Unit) {
    val navController = findNavController()
    val appBarConfiguration = AppBarConfiguration(navController.graph)
    toolbar.run {
        title?.let{ this.title = it }
        setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.action_sign_out) {
                signOut()
                findNavController().navigate(R.id.to_AuthFragment_with_anim)
            }
            onOptionsItemSelected(menuItem)
        }
        setupWithNavController(navController, appBarConfiguration)
    }
}

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), message, duration).show()
}