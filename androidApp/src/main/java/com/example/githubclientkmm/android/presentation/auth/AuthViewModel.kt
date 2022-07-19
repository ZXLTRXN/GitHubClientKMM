package com.example.githubclientkmm.android.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubclientkmm.android.R
import com.example.githubclientkmm.android.utils.LocalizeString
import com.example.githubclientkmm.android.utils.makeDevelopersErrorMessage
import com.example.githubclientkmm.android.utils.validateToken
import com.example.githubclientkmm.data.AppRepository
import com.example.githubclientkmm.data.signInResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    val token: MutableStateFlow<String> = MutableStateFlow("")

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Idle)
    val state: StateFlow<State> = _state

    private val _actions: Channel<Action> = Channel(Channel.BUFFERED)
    val actions: Flow<Action> = _actions.receiveAsFlow()

    private var validationIsOn: Boolean = false

    fun signButtonPressed() {
        if (!validationIsOn) {
            startValidation()
            validationIsOn = true
        }

        if (state.value !is State.Idle) return
        trySignIn()
    }

    private fun startValidation() {
        viewModelScope.launch {
            token.collectLatest {
                validate()
            }
        }
    }

    private fun validate(): ValidationState {
        val validationResult = token.value.validateToken()
        when (validationResult) {
            ValidationState.INVALID -> {
                _state.value = (State.InvalidInput(validationResult.reason!!))
            }
            ValidationState.VALID -> {
                _state.value = State.Idle
            }
            ValidationState.EMPTY -> {
                _state.value = (State.InvalidInput(validationResult.reason!!))
            }
        }
        return validationResult
    }

    private fun trySignIn() {
        viewModelScope.launch {
            _state.value = State.Loading
            repository.signInResult(token.value).onSuccess {
                _actions.send(Action.RouteToMain)
                _state.value = State.Idle
            }.onFailure { throwable ->
                _state.value = State.Idle
                val (msg: LocalizeString, code: Int?) = makeDevelopersErrorMessage(throwable as Exception)
                _actions.send(Action.ShowError(msg, code))
            }
        }
    }

    sealed interface State {
        object Idle : State
        object Loading : State
        data class InvalidInput(val reason: LocalizeString) : State
    }

    sealed interface Action {
        data class ShowError(val message: LocalizeString, val code: Int?) : Action
        object RouteToMain : Action
    }

    enum class ValidationState(val reason: LocalizeString?) {
        EMPTY(LocalizeString.Resource(R.string.empty_input)),
        INVALID(LocalizeString.Resource(R.string.invalid_input)),
        VALID(null)
    }
}