package com.example.githubclientkmm.android.presentation.detailInfo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.githubclientkmm.android.utils.LocalizeString
import com.example.githubclientkmm.android.utils.makeErrorMessage
import com.example.githubclientkmm.data.AppRepository
import com.example.githubclientkmm.data.KeyValueStorage
import com.example.githubclientkmm.data.getRepositoryReadmeResult
import com.example.githubclientkmm.data.getRepositoryResult
import com.example.githubclientkmm.data.models.Repo
import com.example.githubclientkmm.data.network.NotFoundException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RepositoryInfoViewModel @Inject constructor(
    private val repository: AppRepository,
    private val storage: KeyValueStorage,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state: StateFlow<State> = _state.asStateFlow()

    private val ownerName: String = requireNotNull(savedStateHandle.get<String>("ownerName"))
    private val repoName: String = requireNotNull(savedStateHandle.get<String>("repoName"))
    private val branch: String = requireNotNull(savedStateHandle.get<String>("branch"))

    init {
        tryGetInfo()
    }

    fun signOutPressed() {
        storage.authToken = null
    }

    fun reloadPressed() = tryGetInfo()

    private fun tryGetInfo() {
        getInfo(ownerName, repoName, branch)
    }

    private fun getInfo(ownerName: String, repoName: String, branch: String) {
        viewModelScope.launch {
            _state.value = State.Loading
            val (repoRes: Result<Repo>, readmeRes: Result<String>) = coroutineScope {
                val repo = async { repository.getRepositoryResult(ownerName, repoName) }
                val readme =
                    async { repository.getRepositoryReadmeResult(ownerName, repoName, branch) }
                repo.await() to readme.await()
            }

            repoRes.onSuccess { repo ->
                val readmeState = defineReadmeState(readmeRes)
                _state.value = State.Loaded(repo, readmeState)
            }.onFailure { exception ->
                val (icon, label, message) = makeErrorMessage(exception)
                _state.value = State.Error(icon, label, message)
            }
        }
    }

    private fun defineReadmeState(readmeRes: Result<String>): ReadmeState {
        readmeRes.onSuccess { readme ->
            return if (readme.isEmpty()) ReadmeState.Empty
            else ReadmeState.Loaded(readme)
        }.onFailure { exception ->
            if (exception is NotFoundException) return ReadmeState.Empty
            val (icon, label, message) = makeErrorMessage(exception)
            return ReadmeState.Error(icon, label, message)
        }
        return ReadmeState.Empty // unattainable, compiler requires return statement
    }

    sealed interface State {
        object Loading : State
        data class Error(
            val errorIcon: Int,
            val errorLabel: LocalizeString,
            val errorMessage: LocalizeString
        ) : State

        data class Loaded(
            val githubRepo: Repo,
            val readmeState: ReadmeState
        ) : State
    }

    sealed interface ReadmeState {
        object Loading : ReadmeState
        object Empty : ReadmeState
        data class Error(
            val errorIcon: Int,
            val errorLabel: LocalizeString,
            val errorMessage: LocalizeString
        ) : ReadmeState

        data class Loaded(val markdown: String) : ReadmeState
    }


}