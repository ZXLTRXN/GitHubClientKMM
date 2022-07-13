package com.example.githubclientkmm.android.presentation.repositoriesList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubclientkmm.android.R
import com.example.githubclientkmm.android.utils.LocalizeString
import com.example.githubclientkmm.android.utils.makeErrorMessage
import com.example.githubclientkmm.data.AppRepository
import com.example.githubclientkmm.data.KeyValueStorage
import com.example.githubclientkmm.data.getRepositoriesResult
import com.example.githubclientkmm.data.models.Repo
import com.example.githubclientkmm.data.network.ConnectionException
import com.example.githubclientkmm.data.network.RequestCode
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RepositoriesListViewModel @Inject constructor(
    private val repository: AppRepository,
    private val storage: KeyValueStorage
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state: StateFlow<State> = _state.asStateFlow()

    init {
        getRepos()
    }

    fun signOut() {
        storage.authToken = null
    }

    fun retry() = getRepos()

    private fun getRepos() {
        viewModelScope.launch {
            _state.value = State.Loading
            repository.getRepositoriesResult().onSuccess { data ->
                if (data.isEmpty()) {
                    _state.value = State.Empty
                } else {
                    _state.value = State.Loaded(repos = data)
                }
            }.onFailure { exception ->
                val (code, msgId) = when (exception) {
                    is ConnectionException -> RequestCode.NO_CONNECTION to R.string.network_error
                    else -> null to R.string.something_error
                }
                val (icon, label) = makeErrorMessage(code)
                _state.value = State.Error(icon, label, LocalizeString.Resource(msgId))
            }
        }
    }

    sealed interface State {
        object Loading : State
        data class Loaded(val repos: List<Repo>) : State
        data class Error(
            val errorIcon: Int,
            val errorLabel: LocalizeString,
            val errorMessage: LocalizeString
        ) : State

        object Empty : State
    }
}
